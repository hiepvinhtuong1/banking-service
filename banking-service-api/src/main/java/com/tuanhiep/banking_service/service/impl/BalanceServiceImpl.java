package com.tuanhiep.banking_service.service.impl;

import com.tuanhiep.banking_service.dto.response.BalanceResponse;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.entity.Balance;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.mapper.BalanceMapper;
import com.tuanhiep.banking_service.repository.AccountRepository;
import com.tuanhiep.banking_service.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BalanceMapper balanceMapper;

    @Override
    public BalanceResponse getBalanceOfAccount(String accountId) {

        Account existedAccount = accountRepository.findById(accountId)
                .orElseThrow( () -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (existedAccount.isDestroy()) {
            throw new AppException(ErrorCode.ACCOUNT_IS_DELETED);
        }

        if (!existedAccount.isActive()) {
            throw  new AppException(ErrorCode.ACCOUNT_IS_NOT_ACTIVE);
        }

        Balance balanceOfAccount = existedAccount.getBalance();
        if (balanceOfAccount == null) {
            throw new AppException(ErrorCode.BALANCE_NOT_FOUND);
        }

        return balanceMapper.toBalanceResponse(balanceOfAccount);
    }
}
