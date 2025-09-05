package com.tuanhiep.banking_service.dto.request;

import com.tuanhiep.banking_service.exception.ErrorCode;
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
public class PaymentRequest {

     @NotBlank(message = "ErrorCode.ACCOUNT_ID_NOT_BLANK")
     String accountId;

     @NotBlank(message = "ErrorCode.FROM_CARD_NUMBER_NOT_BLANK") // dùng mã lỗi
     @Pattern(regexp = "\\d+", message = "ErrorCode.INVALID_FROM_CARD_NUMBER_FORMAT")
     String fromCardNumber;

     @NotBlank(message = "ErrorCode.TO_CARD_NUMBER_NOT_BLANK")
     @Pattern(regexp = "\\d+", message = "ErrorCode.INVALID_TO_CARD_NUMBER_FORMAT")
     String toCardNumber;

     @NotNull(message = "ErrorCode.AMOUNT_NOT_NULL")
     @DecimalMin(value = "1", inclusive = true, message = "ErrorCode.AMOUNT_GREATER_THAN_ZERO")
     @Digits(integer = 15, fraction = 0, message = "ErrorCode.AMOUNT_MUST_BE_INTEGER")
     BigDecimal amount;

     @NotBlank(message = "ErrorCode.EMAIL_NOT_BLANK")
     String email;

     @NotBlank(message = "ErrorCode.OTP_CODE_NOT_BLANK")
     String otpCode;


}
