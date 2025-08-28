package com.tuanhiep.banking_service.service.impl;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.repository.InvalidatedTokenRepository;
import com.tuanhiep.banking_service.service.JwtService;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
public class JwtServiceImpl implements JwtService {

    @NonFinal
    @Value("${jwt.ACCESS_TOKEN_SIGNER_KEY}")
    String accessTokenSignerKey;

    @NonFinal
    @Value("${jwt.REFRESH_TOKEN_SIGNER_KEY}")
    String refreshTokenSignerKey;

    @NonFinal
    @Value("${jwt.ACCESS_TOKEN_DURATION}")
    int accessTokenDuration;

    @NonFinal
    @Value("${jwt.REFRESH_TOKEN_DURATION}")
    int refreshTokenDuration;

    @Autowired
    private InvalidatedTokenRepository invalidatedTokenRepository;

    private SignedJWT verifyAndParseToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        String signerKey = isRefresh ? refreshTokenSignerKey : accessTokenSignerKey;

        // check token is created by server using HMAC(hash base message authentication) using secret key
        JWSVerifier verifier = new MACVerifier(signerKey);
        SignedJWT signedJWT = SignedJWT.parse(token);
        boolean verified = signedJWT.verify(verifier);

        // check verify token
        if (!verified) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        // check token disable
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    @Override
    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        SignedJWT signedJWT = verifyAndParseToken(token, isRefresh);

        // check expiration time of token
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        if (expirationTime == null || !expirationTime.after(new Date())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED_EXCEPTION);
        }

        // check token disable
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;
    }

    @Override
    public SignedJWT getSignedJWT(String token, boolean isRefresh) throws ParseException, JOSEException {
        return verifyAndParseToken(token, isRefresh);
    }

    @Override
    public String generateAccessToken(Account account) {
        // Xác định thuật toán dùng để mã hõa
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // Xây dựng payload => dùng claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getAccountId()) // đại diện cho user đăng nhập
                .issuer("tuanhiepdev.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(accessTokenDuration, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("scope",buildScope(account) )
                .claim("email", account.getEmail())
                .jwtID(UUID.randomUUID().toString())
                .build();

        try {
            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
            JWSSigner signer = new MACSigner(accessTokenSignerKey.getBytes());
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_CANNOT_CREATE_EXCEPTION);
        }
    }

    @Override
    public String generateRefreshToken(Account account) {
        // Xác định thuật toán dùng để mã hõa
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        // Xây dựng payload => dùng claim
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getAccountId())
                .issuer("tuanhiepdev.com")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(refreshTokenDuration, ChronoUnit.SECONDS).toEpochMilli()
                ))
                .claim("scope",buildScope(account) )
                .claim("email", account.getEmail())
                .jwtID(UUID.randomUUID().toString())
                .build();

        try {
            SignedJWT signedJWT = new SignedJWT(jwsHeader, jwtClaimsSet);
            JWSSigner signer = new MACSigner(refreshTokenSignerKey.getBytes());
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.TOKEN_CANNOT_CREATE_EXCEPTION);
        }
    }

    private String buildScope(Account account) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(account.getRoles())) {
            account.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_"+role.getName());
                if (!CollectionUtils.isEmpty(role.getPermissions())) {
                    role.getPermissions().forEach(permission ->
                            stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }

}
