package com.tuanhiep.banking_service.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AccountCreationRequest {

    @NotBlank(message = "EMAIL_NOT_BLANK")
    @Email(message = "INVALID_EMAIL") // check theo chuẩn RFC 5322 cơ bản
    @Pattern(
            regexp = "^\\S+@\\S+\\.\\S+$",
            message = "EMAIL_INVALID_FORMAT"
    ) // nếu muốn strict giống JS rule
    String email;

    @NotBlank(message = "PHONE_NUMBER_NOT_BLANK")
    @Pattern(
            regexp = "^[0-9]{10,15}$",
            message = "INVALID_PHONE_NUMBER"
    )
    String phoneNumber;

    @NotBlank(message = "PASSWORD_NOT_BLANK")
    @Pattern(
            regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[A-Za-z\\d\\W]{8,256}$",
            message = "INVALID_PASSWORD_FORMAT"
    )
    String password;
}
