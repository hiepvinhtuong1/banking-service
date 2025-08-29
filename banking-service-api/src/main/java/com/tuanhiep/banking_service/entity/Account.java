package com.tuanhiep.banking_service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity(name = "accounts")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Account extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String accountId;

    @NotBlank(message = "CUSTOMER_NAME_NOT_BLANK")
    String customerName;

    @Email(message = "INVALID EMAIL")
    @NotBlank(message = "EMAIL_NOT_BLANK")
    @Column(name = "email", unique = true)
    String email;

    @NotBlank(message = "PHONE_NUMBER_NOT_BLANK")
    @Size(min = 10, max = 15, message = "INVALID_PHONE_NUMBER_LENGTH")
    String phoneNumber;

    @NotBlank(message = "PASSWORD_NOT_BLANK")
    @Size(min = 6, message = "INVALID_PASSWORD_LENGTH")
    String password;

    String verifyCode;

    boolean isActive = false;
    boolean isDestroy = false;
    @ManyToMany
    Set<Role> roles;
}
