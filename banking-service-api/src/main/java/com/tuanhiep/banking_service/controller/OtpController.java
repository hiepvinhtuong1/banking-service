package com.tuanhiep.banking_service.controller;


import com.tuanhiep.banking_service.dto.request.VerifyOTPRequest;
import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.service.MailService;
import com.tuanhiep.banking_service.service.MailerSendService;
import com.tuanhiep.banking_service.service.OtpService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/otp")
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public void sendOtp(@RequestParam String email) throws MessagingException {
            String otp = otpService.generateAndSaveOtp(email);
            String subject = "Your OTP for Transaction Confirmation";
            String textContent = "Your OTP is: " + otp + ". It is valid for 5 minutes.";
            String htmlContent = "<h2>Transaction OTP</h2><p>Your OTP is: <strong>" + otp + "</strong>. It is valid for 5 minutes.</p>";
        try {
            mailService.sendHtmlMail(email, subject, htmlContent);
        } catch (MessagingException e) {
            // fallback gửi text nếu lỗi
            mailService.sendTextMail(email, subject, textContent);
        }
    }

    @PostMapping("/verify")
    public APIResponse<Boolean> verifyOtp(@RequestBody @Valid VerifyOTPRequest request) {
        return APIResponse.<Boolean>builder()
                .data(otpService.validateOtp(request))
                .build();
    }
}
