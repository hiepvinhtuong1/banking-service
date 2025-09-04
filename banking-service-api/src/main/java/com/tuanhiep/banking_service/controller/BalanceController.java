package com.tuanhiep.banking_service.controller;

import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.dto.response.BalanceResponse;
import com.tuanhiep.banking_service.entity.Balance;
import com.tuanhiep.banking_service.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balances")
public class BalanceController {

    @Autowired
    BalanceService balanceService;

    @GetMapping("/{accountId}")
    APIResponse<BalanceResponse> getBalanceOfAccount(@PathVariable("accountId") String accountId) {
        return APIResponse.<BalanceResponse>builder()
                .data(balanceService.getBalanceOfAccount(accountId))
                .build();
    }
}
