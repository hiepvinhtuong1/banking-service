    package com.tuanhiep.notification_service.listener;

    import com.fasterxml.jackson.databind.ObjectMapper;
    import com.tuanhiep.notification_service.dto.PaymentMessage;
    import com.tuanhiep.notification_service.service.MailService;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.jms.annotation.JmsListener;
    import org.springframework.stereotype.Component;

    @Slf4j
    @Component
    @RequiredArgsConstructor
    public class NotificationListener {

        @Autowired
         private MailService mailService;

        @JmsListener(destination = "bank-payment-queue")
        public void handlePaymentNotification(String message) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                PaymentMessage payment = mapper.readValue(message, PaymentMessage.class);

                String htmlBody = "<h2>Payment Confirmation</h2>"
                        + "<p>Transaction ID: " + payment.getTransactionId() + "</p>"
                        + "<p>Amount: " + payment.getAmount() + "</p>"
                        + "<p>Status: " + payment.getStatus() + "</p>";

                mailService.sendHtmlMail(
                        payment.getEmail(),   // <-- giờ email động
                        "Payment Confirmation",
                        htmlBody
                );

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


