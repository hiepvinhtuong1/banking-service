package com.tuanhiep.banking_service.service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;
import com.tuanhiep.banking_service.entity.Account;

import java.text.ParseException;

public interface JwtService {
    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;

    SignedJWT getSignedJWT(String token, boolean isRefresh) throws ParseException, JOSEException;

    String generateAccessToken(Account account);

    String generateRefreshToken(Account account);
}
