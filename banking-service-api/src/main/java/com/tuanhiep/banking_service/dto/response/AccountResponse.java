package com.tuanhiep.banking_service.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AccountResponse {
    String accountId;
    String customerName;
    String phoneNumber;
    String verifyCode;
    boolean isActive;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Set<RoleResponse> roles;
}
