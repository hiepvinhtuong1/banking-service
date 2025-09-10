package com.tuanhiep.banking_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tuanhiep.banking_service.dto.request.AccountUpdateRequest;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{transactionId}/approve")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public APIResponse<TransactionResponse> approveTransaction(@PathVariable String transactionId) throws JsonProcessingException {
        return APIResponse.<TransactionResponse>builder()
                .data(transactionService.approveTransaction(transactionId))
                .build();
    }

    @PutMapping("/{transactionId}/reject")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public APIResponse<TransactionResponse> rejectTransaction(@PathVariable String transactionId) throws JsonProcessingException {
        return APIResponse.<TransactionResponse>builder()
                .data(transactionService.rejectTransaction(transactionId))
                .build();
    }

    @GetMapping("/my-transactions")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public APIResponse<Page<TransactionResponse>> getMyTransactions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String transactionId,

            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String transactionStatus) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        // Lấy userId từ SecurityContext (người đang đăng nhập)
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();

        return APIResponse.<Page<TransactionResponse>>builder()
                .data(transactionService.getUserTransactions(pageable, transactionId, transactionType, transactionStatus, userId))
                .build();
    }


}