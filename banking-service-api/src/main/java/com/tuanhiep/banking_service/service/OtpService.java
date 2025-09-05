package com.tuanhiep.banking_service.service;

import com.tuanhiep.banking_service.dto.request.VerifyOTPRequest;

public interface OtpService {
    String generateAndSaveOtp(String email);
    boolean validateOtp(VerifyOTPRequest request);
}