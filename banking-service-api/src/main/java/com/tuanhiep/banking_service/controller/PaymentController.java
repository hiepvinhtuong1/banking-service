package com.tuanhiep.banking_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuanhiep.banking_service.dto.request.DepositRequest;
import com.tuanhiep.banking_service.dto.request.PaymentRequest;
import com.tuanhiep.banking_service.dto.request.WithdrawRequest;
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
    public APIResponse<TransactionResponse> createNewPayment(@Valid @RequestBody PaymentRequest request) throws JsonProcessingException {
        return APIResponse.<TransactionResponse>builder()
                .data(paymentService.createPayment(request))
                .build();
    }

    @PostMapping("/deposit")
    public APIResponse<TransactionResponse> depositPayment(@Valid @RequestBody DepositRequest request) throws JsonProcessingException {
        return APIResponse.<TransactionResponse>builder()
                .data(paymentService.depositPayment(request))
                .build();
    }

    @PostMapping("/withdraw")
    public APIResponse<TransactionResponse> withdrawPayment(@Valid @RequestBody WithdrawRequest request) throws JsonProcessingException {
        return APIResponse.<TransactionResponse>builder()
                .data(paymentService.withdrawPayment(request))
                .build();
    }

}