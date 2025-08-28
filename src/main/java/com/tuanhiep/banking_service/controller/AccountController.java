package com.tuanhiep.banking_service.controller;

import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.service.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @PostMapping
    APIResponse<AccountResponse> createNewAccount(@RequestBody @Valid AccountCreationRequest request) {
        return APIResponse.<AccountResponse>builder()
                .data(accountService.createNew(request))
                .build();
    }

    @GetMapping("/{accountId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    APIResponse<AccountResponse> getAccount(@PathVariable("accountId") String accountId) {
        return APIResponse.<AccountResponse>builder()
                .data(accountService.getAccount(accountId))
                .build();
    }
}
