package com.tuanhiep.banking_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    Page<TransactionResponse> getAllTransactions(Pageable pageable, String transactionId, String transactionType,String transactionStatus);


    TransactionResponse approveTransaction(String transactionId) throws JsonProcessingException;

    TransactionResponse rejectTransaction(String transactionId) throws JsonProcessingException;

    Page<TransactionResponse> getUserTransactions(Pageable pageable, String transactionId , String transactionType, String transactionStatus, String accountId);

}
