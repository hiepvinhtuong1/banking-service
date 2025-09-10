package com.tuanhiep.banking_service.entity;

import com.tuanhiep.banking_service.enums.TransactionStatus;
import com.tuanhiep.banking_service.enums.TransactionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity(name = "transactions")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String  transactionId;

    @NotBlank(message = "ErrorCode.FROM_CARD_NUMBER_NOT_BLANK")
    @Pattern(regexp = "\\d+", message = "ErrorCode.INVALID_FROM_CARD_NUMBER_FORMAT")
    String fromCardNumber;

    @NotBlank(message = "ErrorCode.TO_CARD_NUMBER_NOT_BLANK")
    @Pattern(regexp = "\\d+", message = "ErrorCode.INVALID_TO_CARD_NUMBER_FORMAT")
    String toCardNumber;

    @NotNull(message = "ErrorCode.AMOUNT_NOT_NULL")
    @DecimalMin(value = "1", inclusive = true, message = "ErrorCode.AMOUNT_GREATER_THAN_ZERO")
    @Digits(integer = 15, fraction = 0, message = "ErrorCode.AMOUNT_MUST_BE_INTEGER")
    BigDecimal amount;

    @NotNull(message = "ErrorCode.TRANSACTION_TYPE_NOT_NULL")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type", length = 20)
    TransactionType transactionType;

    @NotNull(message = "ErrorCode.TRANSACTION_STATUS_NOT_NULL")
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status", length = 20)
    TransactionStatus transactionStatus;

    // Lưu accountId thay vì object Account
    @NotBlank(message = "ErrorCode.ACCOUNT_ID_NOT_BLANK")
    @Column(nullable = false)
    String fromAccountId;

    @NotBlank(message = "ErrorCode.ACCOUNT_ID_NOT_BLANK")
    @Column(nullable = false)
    String toAccountId;
}
