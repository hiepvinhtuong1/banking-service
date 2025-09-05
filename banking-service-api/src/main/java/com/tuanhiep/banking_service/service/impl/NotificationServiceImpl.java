package com.tuanhiep.banking_service.service.impl;

import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import com.tuanhiep.banking_service.service.MailerSendService;
import com.tuanhiep.banking_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {
    @Autowired
    private MailerSendService mailerSendService;

    @Override
    @JmsListener(destination = "bank-payment-queue")
    public void notifyAdminPendingTransaction(TransactionResponse message) {
        // Tạo nội dung email
        String subject = "[Banking Service] Transaction Pending Approval";
        String text = String.format(
                "New Transaction Pending Approval%n" +
                        "Transaction ID: %s%n" +
                        "From Card: %s%n" +
                        "To Card: %s%n" +
                        "Amount: %s%n" +
                        "Created At: %s%n" +
                        "Type: %s%n" +
                        "Status: %s",
                message.getTransactionId(),
                message.getFromCardNumber(),
                message.getToCardNumber(),
                message.getAmount(),
                message.getCreatedAt(),
                message.getTransactionType(),
                message.getTransactionStatus()
        );


        String html = String.format(
                "<h3>New transaction pending approval</h3>"
                        + "<p><b>Transaction ID:</b> %s</p>"
                        + "<p><b>From Card:</b> %s</p>"
                        + "<p><b>To Card:</b> %s</p>"
                        + "<p><b>Amount:</b> %s</p>"
                        + "<p><b>Created At:</b> %s</p>"
                        + "<p><b>Status:</b> %s</p>"
                        + "<p><b>Type:</b> %s</p>"

                , // <-- ở đây phải có %s cho transactionStatus
                message.getTransactionId(),
                message.getFromCardNumber(),
                message.getToCardNumber(),
                message.getAmount(),
                message.getCreatedAt(),
                message.getTransactionStatus(),
                message.getTransactionType()// tham số thứ 6
        );


        // Gửi email đến admin
        mailerSendService.sendEmail(
                "buituanhiepvinhtuong@gmail.com",  // có thể config trong application.yml
                "Bùi Tuấn Hiệp",
                subject,
                text,
                html
        );
    }
}
