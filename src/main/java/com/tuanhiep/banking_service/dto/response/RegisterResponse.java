package com.tuanhiep.banking_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterResponse {
    String accountId;
    String customerName;
    String phoneNumber;
    String verifyCode;

    Set<RoleResponse> roles;
}
