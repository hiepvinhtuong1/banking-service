package com.tuanhiep.banking_service.service;

import com.nimbusds.jose.JOSEException;
import com.tuanhiep.banking_service.dto.request.LoginRequest;
import com.tuanhiep.banking_service.dto.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(LoginRequest request) throws JOSEException;



}
