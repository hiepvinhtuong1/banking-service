package com.tuanhiep.banking_service.service;

public interface MailerSendService {
    public void sendEmail(String toEmail, String toName, String subject, String textContent, String htmlContent);
}
