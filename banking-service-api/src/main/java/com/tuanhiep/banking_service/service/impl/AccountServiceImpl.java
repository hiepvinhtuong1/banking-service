package com.tuanhiep.banking_service.service.impl;

import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.entity.Role;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.mapper.AccountMapper;
import com.tuanhiep.banking_service.repository.AccountRepository;
import com.tuanhiep.banking_service.repository.RoleRepository;
import com.tuanhiep.banking_service.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AccountMapper accountMapper;


    @Override
    public AccountResponse createNew(AccountCreationRequest request) {

        if (accountRepository.existsAccountByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.ACCOUNT_PHONE_NUMBER_EXISTED);
        }

        Account newAccount = accountMapper.toAccount(request);
        newAccount.setCustomerName(request.getEmail().split("@")[0]);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newAccount.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION));

        HashSet<Role> roles = new HashSet<>();
        roles.add(role);

        newAccount.setRoles(roles);

        newAccount.setVerifyCode(null);

        return accountMapper.toAccountResponse(accountRepository.save(newAccount));
    }

    @Override
    public AccountResponse getAccount(String accountId) {
        return  accountMapper.toAccountResponse(
                    accountRepository.findById(accountId).orElseThrow(
                            () -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND)));
    }

}
