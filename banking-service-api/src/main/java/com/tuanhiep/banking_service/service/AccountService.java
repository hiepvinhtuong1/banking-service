package com.tuanhiep.banking_service.service;

import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.dto.request.AccountUpdateRequest;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountService {

    AccountResponse createNew(AccountCreationRequest account);

    AccountResponse getAccount(String accountId);

    Page<AccountResponse> getAllAccounts(Pageable pageable, String customerName, String phoneNumber, String email);

    AccountResponse updateAccount(String accountId, AccountUpdateRequest request);

    void deleteAccount(String accountId);
}
