package com.tuanhiep.banking_service.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.tuanhiep.banking_service.dto.request.LoginRequest;
import com.tuanhiep.banking_service.dto.response.LoginResponse;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.repository.AccountRepository;
import com.tuanhiep.banking_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private AccountRepository accountRepository;

    @NonFinal
    private static final String SIGNER_KEY = "kHRt5ItSVuMaNWJDnlwXRpjCygJBo96ssX63ce+9a7iAO0mWEOIaqU5gbBG4GG9B";

    @Override
    public LoginResponse login(LoginRequest request) throws JOSEException {
        var account = accountRepository.findByEmail(request.getEmail()).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticated = passwordEncoder.matches(request.getPassword(), account.getPassword());

        if (!authenticated) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateToken(request.getEmail());

        return LoginResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }

    private String generateToken(String email) throws JOSEException {
        // Xác định thuật toán dùng để mã hõa
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Xây dựng payload => dùng claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email) // đại diện cho user đăng nhập
                .issuer("tuanhiepdev.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new JOSEException(e);
        }
    }
}
