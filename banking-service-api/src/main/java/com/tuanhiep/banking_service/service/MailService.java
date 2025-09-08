package com.tuanhiep.banking_service.service;

import jakarta.mail.MessagingException;

public interface MailService {
    public void sendTextMail(String to, String subject, String textBody);

    public void sendHtmlMail(String to, String subject, String htmlBody) throws MessagingException;
}
