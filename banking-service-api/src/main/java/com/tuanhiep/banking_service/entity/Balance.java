package com.tuanhiep.banking_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "balances")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String balanceId;

    @NotNull(message = "BALANCE_ACCOUNT_NOT_NULL")
    @Min(value = 0, message = "AVAILABLE_BALANCE_NOT_NEGATIVE")
    private Long availableBalance;

    @NotNull(message = "BALANCE_ACCOUNT_NOT_NULL")
    @Min(value = 0, message = "HOLD_BALANCE_NOT_NEGATIVE")
    private Long holdBalance;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    @NotNull(message = "ACCOUNT_NOT_NULL")
    private Account account;
}
