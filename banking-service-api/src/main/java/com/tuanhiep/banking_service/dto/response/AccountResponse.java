package com.tuanhiep.banking_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountResponse {
    private String email;
    String accountId;
    String customerName;
    String phoneNumber;
    String verifyCode;
    boolean active;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Set<RoleResponse> roles;
}
