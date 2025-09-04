package com.tuanhiep.banking_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;

@Entity
@Table(name = "balances")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Balance extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String balanceId;

    @NotNull(message = "BALANCE_ACCOUNT_NOT_NULL")
    @DecimalMin(value = "0.0", inclusive = true, message = "AVAILABLE_BALANCE_NOT_NEGATIVE")
    private BigDecimal availableBalance = BigDecimal.ZERO;

    @NotNull(message = "BALANCE_ACCOUNT_NOT_NULL")
    @DecimalMin(value = "0.0", inclusive = true, message = "HOLD_BALANCE_NOT_NEGATIVE")
    private BigDecimal holdBalance = BigDecimal.ZERO;

    @OneToOne
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    @NotNull(message = "ACCOUNT_NOT_NULL")
    private Account account;
}

