package com.tuanhiep.banking_service.service.impl;

import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.mapper.AccountMapper;
import com.tuanhiep.banking_service.repository.AccountRepository;
import com.tuanhiep.banking_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountMapper accountMapper;


    @Override
    public Account createNew(AccountCreationRequest request) {

        Account newAccount = accountMapper.toAccount(request);
        newAccount.setCustomerName(request.getEmail().split("@")[0]);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newAccount.setPassword(passwordEncoder.encode(request.getPassword()));
        return accountRepository.save(newAccount);
    }

    @Override
    public Account getAccountById(String accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_EXISTED));
    }
}
