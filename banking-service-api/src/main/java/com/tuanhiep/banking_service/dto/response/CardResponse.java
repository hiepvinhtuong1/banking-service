package com.tuanhiep.banking_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tuanhiep.banking_service.enums.CardStatus;
import com.tuanhiep.banking_service.enums.CardType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CardResponse {

    String cardId;
    String cardNumber;
    CardType cardType;
    LocalDate expiryDate;
    CardStatus status;

    // để hiển thị thêm thông tin account (chỉ ID để tránh vòng lặp)
    String accountId;
}