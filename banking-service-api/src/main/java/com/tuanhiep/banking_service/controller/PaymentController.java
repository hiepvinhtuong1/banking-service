package com.tuanhiep.banking_service.controller;

import com.tuanhiep.banking_service.dto.request.PaymentRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PaymentController {

    @Autowired
    private JmsTemplate jmsTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/payment")
    public String processPayment(@Valid @RequestBody PaymentRequest request) {
        try {
            String message = objectMapper.writeValueAsString(request);
            jmsTemplate.convertAndSend("bank-payment-queue", message);
            return "Payment request sent to queue: " + request.getPaymentId();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}