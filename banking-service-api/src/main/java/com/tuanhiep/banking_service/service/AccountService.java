package com.tuanhiep.banking_service.service;

import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.entity.Account;

public interface AccountService {

    AccountResponse createNew(AccountCreationRequest account);

    AccountResponse getAccount(String accountId);
}
