package com.tuanhiep.banking_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuanhiep.banking_service.dto.response.PaymentMessage;
import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.entity.Balance;
import com.tuanhiep.banking_service.entity.Card;
import com.tuanhiep.banking_service.entity.Transaction;
import com.tuanhiep.banking_service.enums.TransactionStatus;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.mapper.TransactionMapper;
import com.tuanhiep.banking_service.repository.AccountRepository;
import com.tuanhiep.banking_service.repository.CardRepository;
import com.tuanhiep.banking_service.repository.TransactionRepository;
import com.tuanhiep.banking_service.service.TransactionService;
import com.tuanhiep.banking_service.service.impl.specification.TransactionSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    JmsTemplate jmsTemplate;

    public Page<TransactionResponse> getAllTransactions(Pageable pageable, String transactionId, String transactionType, String transactionStatus) {
        return transactionRepository.findAll(TransactionSpecification.combineFilters(transactionId, transactionType, transactionStatus), pageable)
                .map(transactionMapper::toTransactionResponse);
    }

    @Override
    @Transactional
    public TransactionResponse approveTransaction(String transactionId) throws JsonProcessingException {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        if (!transaction.getTransactionStatus().equals(TransactionStatus.PENDING)) {
            throw new AppException(ErrorCode.INVALID_TRANSACTION_STATUS_PENDING);
        }

        Account fromAccount = accountRepository.findById(transaction.getFromAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        Card toCard = cardRepository.findCardByCardNumber(transaction.getToCardNumber())
                .orElseThrow(() -> new AppException(ErrorCode.TO_CARD_NOT_FOUND));

        // 1. Giảm holdBalance, trừ availableBalance thật sự
        Balance fromBalance = fromAccount.getBalance();
        fromBalance.setHoldBalance(fromBalance.getHoldBalance().subtract(transaction.getAmount()));

        // 2. Cộng tiền cho toCard
        Account toAccount = toCard.getAccount();
        Balance toBalance = toAccount.getBalance();
        toBalance.setAvailableBalance(toBalance.getAvailableBalance().add(transaction.getAmount()));

        // 3. Cập nhật status
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction updatedTransaction = transactionRepository.save(transaction);

        PaymentMessage paymentMessage = PaymentMessage.builder()
                .transactionId(updatedTransaction.getTransactionId())
                .fromCard(updatedTransaction.getFromCardNumber())
                .toCard(updatedTransaction.getToCardNumber())
                .amount(updatedTransaction.getAmount())
                .status(updatedTransaction.getTransactionStatus().toString())
                .type(updatedTransaction.getTransactionType().toString())
                .email(fromAccount.getEmail()) // <-- gửi email user ở đây
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String msg = mapper.writeValueAsString(paymentMessage);

        jmsTemplate.convertAndSend("bank-payment-queue", msg);


        return transactionMapper.toTransactionResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse rejectTransaction(String transactionId) throws JsonProcessingException {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.TRANSACTION_NOT_FOUND));

        if (!transaction.getTransactionStatus().equals(TransactionStatus.PENDING)) {
            throw new AppException(ErrorCode.INVALID_TRANSACTION_STATUS_PENDING);
        }

        Account fromAccount = accountRepository.findById(transaction.getFromAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        // 1. Hoàn tiền từ holdBalance → availableBalance
        Balance fromBalance = fromAccount.getBalance();
        fromBalance.setHoldBalance(fromBalance.getHoldBalance().subtract(transaction.getAmount()));
        fromBalance.setAvailableBalance(fromBalance.getAvailableBalance().add(transaction.getAmount()));

        // 2. Cập nhật status
        transaction.setTransactionStatus(TransactionStatus.FAILED);
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction updatedTransaction = transactionRepository.save(transaction);

        PaymentMessage paymentMessage = PaymentMessage.builder()
                .transactionId(updatedTransaction.getTransactionId())
                .fromCard(updatedTransaction.getFromCardNumber())
                .toCard(updatedTransaction.getToCardNumber())
                .amount(updatedTransaction.getAmount())
                .status(updatedTransaction.getTransactionStatus().toString())
                .type(updatedTransaction.getTransactionType().toString())
                .email(fromAccount.getEmail()) // <-- gửi email user ở đây
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String msg = mapper.writeValueAsString(paymentMessage);

        jmsTemplate.convertAndSend("bank-payment-queue", msg);
        return transactionMapper.toTransactionResponse(transaction);
    }

    @Override
    public Page<TransactionResponse> getUserTransactions(Pageable pageable,String transactionId ,String transactionType, String transactionStatus, String accountId) {
        return transactionRepository.findAll(TransactionSpecification.combineFilters(
                transactionId, transactionType, transactionStatus, accountId), pageable)
                .map(transactionMapper::toTransactionResponse);
    }


}
