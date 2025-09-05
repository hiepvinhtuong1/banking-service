package com.tuanhiep.banking_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuanhiep.banking_service.dto.request.PaymentRequest;
import com.tuanhiep.banking_service.dto.request.VerifyOTPRequest;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.dto.response.TransactionResponse;
import com.tuanhiep.banking_service.entity.*;
import com.tuanhiep.banking_service.enums.CardStatus;
import com.tuanhiep.banking_service.enums.TransactionStatus;
import com.tuanhiep.banking_service.enums.TransactionType;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.mapper.TransactionMapper;
import com.tuanhiep.banking_service.repository.AccountRepository;
import com.tuanhiep.banking_service.repository.CardRepository;
import com.tuanhiep.banking_service.repository.TransactionRepository;
import com.tuanhiep.banking_service.service.OtpService;
import com.tuanhiep.banking_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    JmsTemplate jmsTemplate;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Autowired
    OtpService otpService;

    @Autowired
    TransactionMapper transactionMapper;

    private static final String ACCOUNT_KEY_PREFIX = "account:";

    @Override
    @Transactional
    public TransactionResponse createPayment(PaymentRequest request) {

        // 1. Validate OTP trước khi làm gì thêm
        VerifyOTPRequest verifyRequest = new VerifyOTPRequest(request.getEmail(), request.getOtpCode());
        boolean isOtpValid = otpService.validateOtp(verifyRequest);

        if (!isOtpValid) {
            throw new AppException(ErrorCode.INVALID_OR_EXPIRED_OTP);
        }

        Account fromAccount = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        Card fromCard = cardRepository.findCardByCardNumber(request.getFromCardNumber())
                .orElseThrow(() -> new AppException(ErrorCode.FROM_CARD_NOT_FOUND));

        Card toCard = cardRepository.findCardByCardNumber(request.getToCardNumber())
                .orElseThrow(() -> new AppException(ErrorCode.TO_CARD_NOT_FOUND));

        // validate trạng thái thẻ, hạn sử dụng, số dư...
        validateCardsAndBalance(fromCard, toCard, request.getAmount(), fromAccount );

        // Cập nhật balance: giữ tiền
        Balance fromBalance = fromAccount.getBalance();
        fromBalance.setAvailableBalance(fromBalance.getAvailableBalance().subtract(request.getAmount()));
        fromBalance.setHoldBalance(fromBalance.getHoldBalance().add(request.getAmount()));

        // Tạo transaction với status = PENDING
        Transaction transaction = Transaction.builder()
                .fromCardNumber(fromCard.getCardNumber())
                .toCardNumber(toCard.getCardNumber())
                .amount(request.getAmount())
                .transactionType(TransactionType.TRANSFER) // hoặc TRANSFER
                .transactionStatus(TransactionStatus.PENDING)
                .accountId(fromCard.getAccount().getAccountId())
                .build();

        Transaction createdTransaction = transactionRepository.save(transaction);

        // ❌ Invalidate Redis cache (đảm bảo lần sau lấy từ DB mới nhất)
        String key = ACCOUNT_KEY_PREFIX + fromAccount.getAccountId();
        redisTemplate.delete(key);

        jmsTemplate.convertAndSend("bank-payment-queue", transactionMapper.toTransactionResponse(createdTransaction));

            return transactionMapper.toTransactionResponse(createdTransaction);
    }

    private void validateCardsAndBalance(Card from, Card to, BigDecimal amount, Account fromAccount) {
        // 1. Kiểm tra trạng thái thẻ
        if (!from.getStatus().equals(CardStatus.ACTIVE)) {
            throw new AppException(ErrorCode.FROM_CARD_INACTIVE);
        }
        if (!to.getStatus().equals(CardStatus.ACTIVE)) {
            throw new AppException(ErrorCode.TO_CARD_INACTIVE);
        }

        // 2. Kiểm tra hạn sử dụng
        if (from.getExpiryDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.FROM_CARD_EXPIRED);
        }

        // 2. Kiểm tra hạn sử dụng
        if (to.getExpiryDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.TO_CARD_EXPIRED);
        }

        // 3. Kiểm tra số dư
        if (fromAccount.getBalance().getAvailableBalance().compareTo(amount) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        // 4. Kiểm tra hạn mức giao dịch hằng ngày
        UserLevel userLevel = fromAccount.getUserLevel();
        if (userLevel != null) {
            long dailyLimit = userLevel.getDailyTransactionAmount();

            // query TransactionRepository để tính tổng giao dịch hôm nay
            BigDecimal totalToday = transactionRepository.sumTransactionAmountByAccountAndDate(
                    from.getAccount().getAccountId(), LocalDate.now());

            if (totalToday.add(amount).compareTo(BigDecimal.valueOf(dailyLimit)) > 0) {
                throw new AppException(ErrorCode.DAILY_TRANSACTION_LIMIT_EXCEEDED);
            }
        }
    }


}
