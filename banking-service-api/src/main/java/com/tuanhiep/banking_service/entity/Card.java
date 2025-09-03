package com.tuanhiep.banking_service.entity;

import com.tuanhiep.banking_service.enums.CardStatus;
import com.tuanhiep.banking_service.enums.CardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String cardId;

    @NotBlank(message = "CARD_NUMBER_NOT_BLANK")
    @Pattern(regexp = "\\d{8,19}", message = "INVALID_CARD_NUMBER")
    // Thông thường số thẻ có 8–19 số
    @Column(nullable = false, unique = true)
    String cardNumber;

    @NotNull(message = "CARD_TYPE_NOT_NULL")
    @Enumerated(EnumType.STRING) // debit, credit
    CardType cardType;

    @NotNull(message = "EXPIRY_DATE_NOT_NULL")
    @Future(message = "EXPIRY_DATE_MUST_BE_IN_FUTURE")
    LocalDate expiryDate;

    @NotNull(message = "CARD_STATUS_NOT_NULL")
    @Enumerated(EnumType.STRING) // inactive, active
    CardStatus status;

    // Nhiều card thuộc 1 account
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    @NotNull(message = "ACCOUNT_NOT_NULL")
    Account account;
}
