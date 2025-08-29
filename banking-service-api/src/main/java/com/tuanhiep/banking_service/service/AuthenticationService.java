package com.tuanhiep.banking_service.service;

import com.nimbusds.jose.JOSEException;
import com.tuanhiep.banking_service.dto.request.*;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.dto.response.LoginResponse;
import com.tuanhiep.banking_service.dto.response.RefreshTokenResponse;

import java.text.ParseException;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request) ;

    AccountResponse register(AccountCreationRequest request);

    void logout(LogoutRequest request) throws ParseException, JOSEException;

    RefreshTokenResponse refreshToken(RefreshTokenRequest refreshTokenRequest) throws ParseException, JOSEException;

    AccountResponse verifyAccount(VerificationAccountRequest request);
}
