package com.tuanhiep.banking_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class VerifyOTPRequest {
    @NotBlank(message = "ErrorCode.EMAIL_NOT_BLANK")
    String email;

    @NotBlank(message = "ErrorCode.VERIFY_CODE_NOT_BLANK")
    String otpCode;
}
