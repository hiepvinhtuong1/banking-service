package com.tuanhiep.banking_service.service;

import com.tuanhiep.banking_service.dto.request.PaymentRequest;
import com.tuanhiep.banking_service.dto.response.TransactionResponse;

public interface PaymentService {
    TransactionResponse createPayment(PaymentRequest paymentRequest);

}
