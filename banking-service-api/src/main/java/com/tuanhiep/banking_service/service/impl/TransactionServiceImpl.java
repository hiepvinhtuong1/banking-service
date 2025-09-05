package com.tuanhiep.banking_service.service.impl;

import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import com.tuanhiep.banking_service.mapper.TransactionMapper;
import com.tuanhiep.banking_service.repository.TransactionRepository;
import com.tuanhiep.banking_service.service.TransactionService;
import com.tuanhiep.banking_service.service.impl.specification.TransactionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    public Page<TransactionResponse> getAllTransactions(Pageable pageable, String transactionId, String transactionType, String transactionStatus) {
        return transactionRepository.findAll(TransactionSpecification.combineFilters(transactionId, transactionType, transactionStatus), pageable)
                .map(transactionMapper::toTransactionResponse);
    }
}
