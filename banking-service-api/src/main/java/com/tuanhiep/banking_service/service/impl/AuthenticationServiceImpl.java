package com.tuanhiep.banking_service.service.impl;

import com.nimbusds.jose.*;

import com.nimbusds.jwt.SignedJWT;
import com.tuanhiep.banking_service.dto.request.*;
import com.tuanhiep.banking_service.dto.response.*;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.entity.Balance;
import com.tuanhiep.banking_service.entity.InvalidatedToken;
import com.tuanhiep.banking_service.entity.Role;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.mapper.AccountMapper;
import com.tuanhiep.banking_service.repository.*;
import com.tuanhiep.banking_service.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    @Autowired
    private UserLeveLRepository userLeveLRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    MailerSendServiceImpl mailerSendService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtServiceImpl jwtService;

    @Autowired
    private AccountMapper accountMapper;

    @Override
    public LoginResponse login(LoginRequest request){
        var account = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

            if (!passwordEncoder.matches(request.getPassword(), account.getPassword())){
            throw new AppException(ErrorCode.EMAIL_OR_PASSWORD_NOT_MATCH);
        }

        if (!account.isActive()) {
            throw new AppException(ErrorCode.ACCOUNT_IS_NOT_ACTIVE);
        }

        if (account.isDestroy()) {
            throw new AppException(ErrorCode.ACCOUNT_IS_DELETED);
        }


        return LoginResponse.builder()
                .accessToken(jwtService.generateAccessToken(account))
                .refreshToken(jwtService.generateRefreshToken(account))
                .accountResponse(accountMapper.toAccountResponse(account))
                .build();

    }

    @Override
    public AccountResponse register(AccountCreationRequest request) {
        if (accountRepository.existsAccountByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
        }

        if (accountRepository.existsAccountByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.ACCOUNT_PHONE_NUMBER_EXISTED);
        }

        Account newAccount = accountMapper.toAccount(request);
        newAccount.setCustomerName(request.getEmail().split("@")[0]);

        // UserLevel
        Integer userLevelId = request.getUserLevelId();
        if (userLevelId == null) {
            userLevelId = 1; // mặc định
        } else if (!userLeveLRepository.existsById(userLevelId)) {
            throw new AppException(ErrorCode.USER_LEVEL_NOT_FOUND);
        }
        newAccount.setUserLevel(userLeveLRepository.findById(userLevelId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_LEVEL_NOT_FOUND)));

        newAccount.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        HashSet<Role> roles = new HashSet<>();
        roles.add(role);

        newAccount.setRoles(roles);

        newAccount.setVerifyCode(UUID.randomUUID().toString());

        Account createdAccount = null;

        try {
            createdAccount  = accountRepository.save(newAccount);
        } catch (DataIntegrityViolationException e) {
            throw new AppException(ErrorCode.ACCOUNT_EMAIL_EXISTED);
        }

        // 👉 Save Account trước
        Account savedAccount = accountRepository.save(newAccount);

        // 👉 Khởi tạo Balance mặc định
        Balance balance = new Balance();
        balance.setAvailableBalance(BigDecimal.ZERO);
        balance.setHoldBalance(BigDecimal.ZERO);
        balance.setCreatedAt(LocalDateTime.now());
        balance.setUpdatedAt(LocalDateTime.now());
        balance.setAccount(savedAccount);

        balanceRepository.save(balance);

        // 👉 Gắn balance lại vào account để mapping đầy đủ
        savedAccount.setBalance(balance);



        // Gửi email cho người dùng xác thực tài khoản

        // Tạo verificationLink với email và code động
        String verificationLink = "http://localhost:5173/account/verification?email=" + createdAccount.getEmail() + "&code=" + createdAccount.getVerifyCode();

        // Gửi email với verificationLink
        mailerSendService.sendEmail(
                createdAccount.getEmail(),
                createdAccount.getCustomerName(),
                "Xác thực tài khoản của bạn",
                "Vui lòng nhấp vào liên kết sau để xác thực tài khoản: " + verificationLink,
                "<h1>Xác thực tài khoản</h1><p>Vui lòng nhấp vào liên kết sau để xác thực tài khoản của bạn: <a href='" + verificationLink + "'>Xác thực ngay</a></p>"
        );

        return accountMapper.toAccountResponse(createdAccount);
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException {
        // check access token is expired

        SignedJWT signedJWTAccessToken = jwtService.getSignedJWT(refreshTokenRequest.getAccessToken(), false);

        if (signedJWTAccessToken.getJWTClaimsSet().getExpirationTime().after(new Date())) {
            throw new AppException(ErrorCode.ACCESS_TOKEN_STILL_VALID);
        }

        SignedJWT signedJWTRefreshToken = jwtService.verifyToken(refreshTokenRequest.getRefreshToken(), true);

        // check token and refresh token from one user
        String accountId = signedJWTRefreshToken.getJWTClaimsSet().getSubject();
        if (!accountId.equals(signedJWTRefreshToken.getJWTClaimsSet().getSubject())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String refreshTokenId = signedJWTRefreshToken.getJWTClaimsSet().getJWTID();
        Date refreshTokenExpiration = signedJWTRefreshToken.getJWTClaimsSet().getExpirationTime();

        // if token still valid
        if (refreshTokenExpiration.after(new Date())) {
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(refreshTokenId)
                    .expireTime(refreshTokenExpiration)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }


        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        return RefreshTokenResponse.builder()
                .accessToken(jwtService.generateAccessToken(account))
                .refreshToken(jwtService.generateRefreshToken(account))
                .build();
    }

    @Override
    public AccountResponse verifyAccount(VerificationAccountRequest request) {
        Account exsitedAccount = accountRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND)
        );

        if (exsitedAccount.isActive()) {
            throw new AppException(ErrorCode.ACCOUNT_IS_ACTIVE);
        }

        if (!exsitedAccount.getVerifyCode().equals(request.getCode())) {
            throw new AppException(ErrorCode.VERIFY_CODE_NOT_MATCH);
        }

        exsitedAccount.setActive(true);
        exsitedAccount.setVerifyCode(null);
        exsitedAccount.setUpdatedAt(LocalDateTime.now());

        return accountMapper.toAccountResponse(accountRepository.save(exsitedAccount));

    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        SignedJWT signedAccessToken = jwtService.getSignedJWT(request.getAccessToken(), false);
        SignedJWT signedRefreshToken = jwtService.getSignedJWT(request.getRefreshToken(), true);
        String accessTokenUserId = signedAccessToken.getJWTClaimsSet().getSubject();
        String refreshTokenUserId = signedRefreshToken.getJWTClaimsSet().getSubject();

        // check same userId
        if (!accessTokenUserId.equals(refreshTokenUserId)) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        Date expiryTimeAccessToken = signedAccessToken.getJWTClaimsSet().getExpirationTime();
        String accessTokenJWTID = signedAccessToken.getJWTClaimsSet().getJWTID();
        Date expiryTimeRefreshToken = signedRefreshToken.getJWTClaimsSet().getExpirationTime();
        String refreshTokenJWTID = signedRefreshToken.getJWTClaimsSet().getJWTID();

        if (expiryTimeAccessToken.after(new Date())) {
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(accessTokenJWTID)
                    .expireTime(expiryTimeAccessToken)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }

        if (expiryTimeRefreshToken.after(new Date())) {
            InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                    .id(refreshTokenJWTID)
                    .expireTime(expiryTimeRefreshToken)
                    .build();
            invalidatedTokenRepository.save(invalidatedToken);
        }
    }

}
