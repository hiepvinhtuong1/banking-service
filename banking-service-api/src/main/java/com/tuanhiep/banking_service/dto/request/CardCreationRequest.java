package com.tuanhiep.banking_service.dto.request;

import com.tuanhiep.banking_service.enums.CardStatus;
import com.tuanhiep.banking_service.enums.CardType;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardCreationRequest {

    @NotNull(message = "ACCOUNT_ID_NOT_NULL")
    String accountId;  // cần biết tạo thẻ cho account nào

    @NotNull(message = "CARD_TYPE_NOT_NULL")
    CardType cardType;

    @NotNull(message = "EXPIRY_DATE_NOT_NULL")
    @Future(message = "EXPIRY_DATE_MUST_BE_IN_FUTURE")
    LocalDate expiryDate;

    @NotNull(message = "CARD_STATUS_NOT_NULL")
    CardStatus status;
}
