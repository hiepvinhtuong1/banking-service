package com.tuanhiep.banking_service.controller;


import com.tuanhiep.banking_service.service.MailerSendService;
import com.tuanhiep.banking_service.service.OtpService;
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
    public String sendOtp(@RequestParam String email) {
        try {
            String otp = otpService.generateAndSaveOtp(email);
            String subject = "Your OTP for Transaction Confirmation";
            String textContent = "Your OTP is: " + otp + ". It is valid for 5 minutes.";
            String htmlContent = "<h2>Transaction OTP</h2><p>Your OTP is: <strong>" + otp + "</strong>. It is valid for 5 minutes.</p>";
            mailerSendService.sendEmail(email, "Customer", subject, textContent, htmlContent);
            return "OTP sent to " + email;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    @PostMapping("/verify")
    public boolean verifyOtp(@RequestParam String email, @RequestParam String otp) {
        return otpService.validateOtp(email, otp);
    }
}
