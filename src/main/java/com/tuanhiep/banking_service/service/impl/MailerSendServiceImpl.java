package com.tuanhiep.banking_service.service.impl;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailerSendServiceImpl {
    @Value("${mailersend.api.token}")
    private String apiToken;

    @Value("${mailersend.from.email}")
    private String fromEmail;

    @Value("${mailersend.from.name}")
    private String fromName;

    public void sendEmail(String toEmail, String toName, String subject, String textContent, String htmlContent) {
        MailerSend mailerSend = new MailerSend();
        mailerSend.setToken(apiToken);

        Email email = new Email();
        email.setFrom(fromName, fromEmail);  // Set sender
        email.addRecipient(toName, toEmail);  // Set recipient (có thể thêm nhiều bằng cách gọi lại)

        // Optional: Thêm CC hoặc BCC
        // email.addCc("CC Name", "cc@example.com");
        // email.addBcc("BCC Name", "bcc@example.com");

        email.setSubject(subject);
        email.setPlain(textContent);  // Nội dung plain text (fallback)
        email.setHtml(htmlContent);   // Nội dung HTML (nếu có)

        // Optional: Thêm attachment (nếu cần)
        // email.addAttachment(new File("path/to/file.pdf"), "filename.pdf");

        // Optional: Sử dụng template ID từ MailerSend dashboard
        // email.setTemplateId("your-template-id");

        try {
            mailerSend.emails().send(email);
            System.out.println("Email sent successfully!");
        } catch (MailerSendException e) {
            // Xử lý lỗi: e.getCode(), e.getErrors()
            System.err.println("Error sending email: " + e.getMessage());
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
