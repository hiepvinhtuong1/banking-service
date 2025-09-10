package com.tuanhiep.banking_service.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentMessage {
    private String transactionId;
    private String fromCard;
    private String toCard;
    private BigDecimal amount;
    private String status;
    private String type;
    private String email;
}

