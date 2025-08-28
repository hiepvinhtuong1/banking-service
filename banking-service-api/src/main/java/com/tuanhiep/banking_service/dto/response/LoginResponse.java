package com.tuanhiep.banking_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class LoginResponse {
    String accessToken;
    String refreshToken;
    AccountResponse accountResponse;
}
