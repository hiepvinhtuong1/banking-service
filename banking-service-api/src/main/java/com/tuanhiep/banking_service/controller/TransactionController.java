package com.tuanhiep.banking_service.controller;

import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import com.tuanhiep.banking_service.enums.CardStatus;
import com.tuanhiep.banking_service.enums.CardType;
import com.tuanhiep.banking_service.service.TransactionService;
import com.tuanhiep.banking_service.enums.TransactionType;
import com.tuanhiep.banking_service.enums.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    APIResponse<Page<TransactionResponse>> getAllTransactions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String transactionId,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String transactionStatus) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return APIResponse.<Page<TransactionResponse>>builder()
                .data(transactionService.getAllTransactions(pageable, transactionId, transactionType, transactionStatus))
                .build();
    }

    @GetMapping("/types")
    public APIResponse<TransactionType[]> getTransactionTypes() {
        return APIResponse.<TransactionType[]>builder()
                .data(TransactionType.values())
                .build();
    }

    @GetMapping("/status")
    public APIResponse<TransactionStatus[]> getTransactionStatus() {
        return APIResponse.<TransactionStatus[]>builder()
                .data(TransactionStatus.values())
                .build();
    }
}