package com.tuanhiep.banking_service.service;

import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    Page<TransactionResponse> getAllTransactions(Pageable pageable, String transactionId, String transactionType,String transactionStatus);

}
