package com.tuanhiep.banking_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class WithdrawRequest {

    @NotBlank(message = "ErrorCode.CARD_NUMBER_NOT_BLANK")
    @Pattern(regexp = "\\d+", message = "ErrorCode.INVALID_CARD_NUMBER_FORMAT")
    String cardNumber; // thẻ rút tiền

    @NotNull(message = "ErrorCode.AMOUNT_NOT_NULL")
    @DecimalMin(value = "1", inclusive = true, message = "ErrorCode.AMOUNT_GREATER_THAN_ZERO")
    @Digits(integer = 15, fraction = 0, message = "ErrorCode.AMOUNT_MUST_BE_INTEGER")
    BigDecimal amount;

}
