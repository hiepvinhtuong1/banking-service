package com.tuanhiep.banking_service.service;

import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.dto.response.BalanceResponse;

public interface BalanceService {
    BalanceResponse getBalanceOfAccount(String accountId);
}
