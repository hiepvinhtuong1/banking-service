package com.tuanhiep.banking_service.service;

public interface OtpService {
    String generateAndSaveOtp(String email);
    boolean validateOtp(String email, String otp);
}