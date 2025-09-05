package com.tuanhiep.banking_service.controller;


import com.tuanhiep.banking_service.dto.request.VerifyOTPRequest;
import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.service.MailerSendService;
import com.tuanhiep.banking_service.service.OtpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private MailerSendService mailerSendService;

    @PostMapping("/send")
    public void sendOtp(@RequestParam String email) {
            String otp = otpService.generateAndSaveOtp(email);
            String subject = "Your OTP for Transaction Confirmation";
            String textContent = "Your OTP is: " + otp + ". It is valid for 5 minutes.";
            String htmlContent = "<h2>Transaction OTP</h2><p>Your OTP is: <strong>" + otp + "</strong>. It is valid for 5 minutes.</p>";
            mailerSendService.sendEmail(email, "Customer", subject, textContent, htmlContent);
    }

    @PostMapping("/verify")
    public APIResponse<Boolean> verifyOtp(@RequestBody @Valid VerifyOTPRequest request) {
        return APIResponse.<Boolean>builder()
                .data(otpService.validateOtp(request))
                .build();
    }
}
