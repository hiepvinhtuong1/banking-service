package com.tuanhiep.banking_service.controller;

import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.dto.request.AccountUpdateRequest;
import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.service.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;


@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    APIResponse<AccountResponse> createNewAccount(@RequestBody @Valid AccountCreationRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

// Lấy username
        String username = authentication.getName();

// Lấy quyền
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            System.out.println("ROLE = " + authority.getAuthority());
        }

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

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    APIResponse<Page<AccountResponse>> getAllAccounts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String email) {
        Pageable pageable = PageRequest.of(page-1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return APIResponse.<Page<AccountResponse>>builder()
                .data(accountService.getAllAccounts(pageable, customerName, phoneNumber, email))
                .build();
    }

    @PutMapping("/{accountId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    APIResponse<AccountResponse> updateAccount(@PathVariable("accountId") String accountId,
                                               @RequestBody AccountUpdateRequest request) {
        return APIResponse.<AccountResponse>builder()
                .data(accountService.updateAccount(accountId,request))
                .build();
    }

    @DeleteMapping("/{accountId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    APIResponse<String> deleteAccount(@PathVariable("accountId") String accountId) {
        accountService.deleteAccount(accountId);
        return APIResponse.<String>builder()
                .data("Account deleted successfully (soft delete)")
                .build();
    }

}
