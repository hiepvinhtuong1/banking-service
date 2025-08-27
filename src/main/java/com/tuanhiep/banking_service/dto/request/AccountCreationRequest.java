package com.tuanhiep.banking_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AccountCreationRequest {
    @Email(message = "INVALID EMAIL")
    @NotBlank(message = "EMAIL_NOT_BLANK")
    String email;

    @NotBlank(message = "PHONE_NUMBER_NOT_BLANK")
    @Size(min = 10, max = 15, message = "INVALID_PHONE_NUMBER_LENGTH")
    String phoneNumber;

    @NotBlank(message = "PASSWORD_NOT_BLANK")
    @Size(min = 6, message = "INVALID_PASSWORD_LENGTH")
    String password;
}
