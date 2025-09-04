package com.tuanhiep.banking_service.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuanhiep.banking_service.dto.request.PaymentRequest;
import com.tuanhiep.banking_service.service.MailerSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class PaymentListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MailerSendService mailerSendService; // Inject MailerSendService

    @JmsListener(destination = "bank-payment-queue")
    public void receiveMessage(String message) {
        try {
            // Deserialize JSON thành PaymentRequest
            PaymentRequest paymentDto = objectMapper.readValue(message, PaymentRequest.class);

            // Tạo nội dung email
            String emailContent = String.format(
                    "Payment confirmed for paymentId: %s, accountId: %s, amount: %.2f, currency: %s",
                    paymentDto.getPaymentId(), paymentDto.getAccountId(), paymentDto.getAmount(), paymentDto.getCurrency()
            );

            // Gửi email qua MailerSendService
            mailerSendService.sendEmail(
                    "buituanhiepvinhtuong@gmail.com", // Email người nhận
                    "Customer",                       // Tên người nhận
                    "Payment Confirmation",           // Tiêu đề email
                    emailContent,                     // Nội dung plain text
                    emailContent                      // Nội dung HTML (có thể tùy chỉnh sau)
            );

            // Log để kiểm tra
            System.out.println(emailContent);

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}