package com.tuanhiep.banking_service.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuanhiep.banking_service.dto.request.DepositRequest;
import com.tuanhiep.banking_service.dto.request.PaymentRequest;
import com.tuanhiep.banking_service.dto.request.VerifyOTPRequest;
import com.tuanhiep.banking_service.dto.request.WithdrawRequest;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.dto.response.PaymentMessage;
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
import com.tuanhiep.banking_service.service.AccountService;
import com.tuanhiep.banking_service.service.OtpService;
import com.tuanhiep.banking_service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.tuanhiep.banking_service.constant.Constants.ACCOUNT_KEY_PREFIX;

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

    @Autowired
    AccountService accountService;

    @Override
    @Transactional
    public TransactionResponse createPayment(PaymentRequest request) throws JsonProcessingException {

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
                .fromAccountId(fromCard.getAccount().getAccountId())
                .toAccountId(toCard.getAccount().getAccountId())
                .build();

        Transaction createdTransaction = transactionRepository.save(transaction);

        PaymentMessage paymentMessage = PaymentMessage.builder()
                .transactionId(createdTransaction.getTransactionId())
                .fromCard(createdTransaction.getFromCardNumber())
                .toCard(createdTransaction.getToCardNumber())
                .amount(createdTransaction.getAmount())
                .status(createdTransaction.getTransactionStatus().toString())
                .type(createdTransaction.getTransactionType().toString())
                .email("hiepbthe186170@fpt.edu.vn") // <-- gửi email user ở đây
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String msg = mapper.writeValueAsString(paymentMessage);

        jmsTemplate.convertAndSend("bank-payment-queue", msg);


        return transactionMapper.toTransactionResponse(createdTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse depositPayment(DepositRequest request) throws JsonProcessingException {

        // Lấy userId từ SecurityContext (người đang đăng nhập)
        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();


        Card toCard = cardRepository.findCardByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new AppException(ErrorCode.TO_CARD_NOT_FOUND));

        // 3. Validate card
        if (!toCard.getStatus().equals(CardStatus.ACTIVE)) {
            throw new AppException(ErrorCode.TO_CARD_INACTIVE);
        }
        if (toCard.getExpiryDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.TO_CARD_EXPIRED);
        }


        // 4. Cập nhật balance
        Balance balance = toCard.getAccount().getBalance();
        balance.setAvailableBalance(balance.getAvailableBalance().add(request.getAmount()));

        // 5. Tạo transaction
        Transaction transaction = Transaction.builder()
                .fromCardNumber("000000000000")
                .toCardNumber(toCard.getCardNumber())
                .amount(request.getAmount())
                .transactionType(TransactionType.DEPOSIT)
                .transactionStatus(TransactionStatus.SUCCESS)
                .fromAccountId(accountId)
                .toAccountId(toCard.getAccount().getAccountId())
                .build();

        Transaction createdTransaction = transactionRepository.save(transaction);

        // Xoá cache sau khi update
        String key = ACCOUNT_KEY_PREFIX + toCard.getAccount().getAccountId();
        redisTemplate.delete(key);

        // 6. Gửi MQ cho user + chủ thẻ
        sendPaymentMessages(createdTransaction, toCard.getAccount().getEmail(), toCard.getAccount().getEmail());

        return transactionMapper.toTransactionResponse(createdTransaction);
    }

    @Override
    @Transactional
    public TransactionResponse withdrawPayment(WithdrawRequest request) throws JsonProcessingException {

        String accountId = SecurityContextHolder.getContext().getAuthentication().getName();


        Card fromCard = cardRepository.findCardByCardNumber(request.getCardNumber())
                .orElseThrow(() -> new AppException(ErrorCode.FROM_CARD_NOT_FOUND));

        // 3. Validate card
        if (!fromCard.getStatus().equals(CardStatus.ACTIVE)) {
            throw new AppException(ErrorCode.FROM_CARD_INACTIVE);
        }
        if (fromCard.getExpiryDate().isBefore(LocalDate.now())) {
            throw new AppException(ErrorCode.FROM_CARD_EXPIRED);
        }

        // 4. Kiểm tra số dư
        Balance balance = fromCard.getAccount().getBalance();
        if (balance.getAvailableBalance().compareTo(request.getAmount()) < 0) {
            throw new AppException(ErrorCode.INSUFFICIENT_BALANCE);
        }

        // 5. Cập nhật balance
        balance.setAvailableBalance(balance.getAvailableBalance().subtract(request.getAmount()));

        // 6. Tạo transaction
        Transaction transaction = Transaction.builder()
                .fromCardNumber(fromCard.getCardNumber())
                .toCardNumber("000000000000")
                .amount(request.getAmount())
                .transactionType(TransactionType.WITHDRAW)
                .transactionStatus(TransactionStatus.SUCCESS)
                .fromAccountId(fromCard.getAccount().getAccountId())
                .toAccountId(accountId)
                .build();

        Transaction createdTransaction = transactionRepository.save(transaction);

        // Xoá cache sau khi update
        String key = ACCOUNT_KEY_PREFIX + fromCard.getAccount().getAccountId();
        redisTemplate.delete(key);

        // 7. Gửi MQ cho user + chủ thẻ
        sendPaymentMessages(createdTransaction, fromCard.getAccount().getEmail(), fromCard.getAccount().getEmail());

        return transactionMapper.toTransactionResponse(createdTransaction);
    }

    /**
     * Hàm gửi MQ cho cả người request và chủ thẻ
     */
    private void sendPaymentMessages(Transaction transaction, String requesterEmail, String ownerEmail) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        PaymentMessage requesterMsg = PaymentMessage.builder()
                .transactionId(transaction.getTransactionId())
                .fromCard(transaction.getFromCardNumber())
                .toCard(transaction.getToCardNumber())
                .amount(transaction.getAmount())
                .status(transaction.getTransactionStatus().toString())
                .type(transaction.getTransactionType().toString())
                .email(requesterEmail)
                .build();
        jmsTemplate.convertAndSend("bank-payment-queue", mapper.writeValueAsString(requesterMsg));

        PaymentMessage ownerMsg = PaymentMessage.builder()
                .transactionId(transaction.getTransactionId())
                .fromCard(transaction.getFromCardNumber())
                .toCard(transaction.getToCardNumber())
                .amount(transaction.getAmount())
                .status(transaction.getTransactionStatus().toString())
                .type(transaction.getTransactionType().toString())
                .email(ownerEmail)
                .build();



        jmsTemplate.convertAndSend("bank-payment-queue", mapper.writeValueAsString(ownerMsg));
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
