package com.tuanhiep.banking_service.service;

import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.entity.Account;

public interface AccountService {

    public Account createNew(AccountCreationRequest account);

    public Account getAccountById(String accountId);
}
