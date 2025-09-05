package com.tuanhiep.banking_service.controller;

import com.tuanhiep.banking_service.dto.request.PaymentRequest;
import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import com.tuanhiep.banking_service.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @PostMapping
    public APIResponse<TransactionResponse> createNewPayment(@Valid @RequestBody PaymentRequest request) {
        return APIResponse.<TransactionResponse>builder()
                .data(paymentService.createPayment(request))
                .build();
    }
}