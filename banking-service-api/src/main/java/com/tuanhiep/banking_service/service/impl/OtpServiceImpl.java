package com.tuanhiep.banking_service.service.impl;


import com.tuanhiep.banking_service.dto.request.VerifyOTPRequest;
import com.tuanhiep.banking_service.service.OtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpServiceImpl implements OtpService {
    private static final String OTP_PREFIX = "otp:";
    private static final long OTP_EXPIRY_MINUTES = 5;
    private static final int OTP_LENGTH = 6;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String generateAndSaveOtp(String email) {
        // Tạo OTP ngẫu nhiên
        String otp = generateRandomOtp();
        // Lưu OTP vào Redis với TTL
        redisTemplate.opsForValue().set(OTP_PREFIX + email, otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        return otp;
    }

    @Override
    public boolean validateOtp(VerifyOTPRequest request) {
        String storedOtp = redisTemplate.opsForValue().get(OTP_PREFIX + request.getEmail());
        if (storedOtp == null || !storedOtp.equals(request.getOtpCode())) {
            return false;
        }
        // Xóa OTP sau khi xác thực
        redisTemplate.delete(OTP_PREFIX + request.getEmail());
        return true;
    }

    private String generateRandomOtp() {
        SecureRandom random = new SecureRandom();
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }
}
