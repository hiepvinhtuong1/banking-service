package com.tuanhiep.banking_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuanhiep.banking_service.dto.request.DepositRequest;
import com.tuanhiep.banking_service.dto.request.PaymentRequest;
import com.tuanhiep.banking_service.dto.request.WithdrawRequest;
import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import jakarta.validation.Valid;

public interface PaymentService {
    TransactionResponse createPayment(PaymentRequest paymentRequest) throws JsonProcessingException;

    TransactionResponse depositPayment(DepositRequest request) throws JsonProcessingException;
    TransactionResponse withdrawPayment(WithdrawRequest request) throws JsonProcessingException;

}
