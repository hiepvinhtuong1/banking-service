package com.tuanhiep.banking_service.configuration;

import com.tuanhiep.banking_service.constant.PredefinedPermission;
import com.tuanhiep.banking_service.constant.PredefinedRole;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.entity.Permission;
import com.tuanhiep.banking_service.entity.Role;
import com.tuanhiep.banking_service.entity.UserLevel;
import com.tuanhiep.banking_service.repository.AccountRepository;
import com.tuanhiep.banking_service.repository.PermissionRepository;
import com.tuanhiep.banking_service.repository.RoleRepository;
import com.tuanhiep.banking_service.repository.UserLeveLRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_EMAIL = "admin@gmail.com";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @NonFinal
    static final String ADMIN_CUSTOMER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PHONE_NUMBER = "0000000000";

    @Bean
    ApplicationRunner applicationRunner(AccountRepository accountRepository, RoleRepository roleRepository,
                                        PermissionRepository permissionRepository, UserLeveLRepository userLevelRepository) {
        log.info("Initializing application.....");
        return args -> {
            // Create UserLevel records if they don't exist
            if (userLevelRepository.findByName("VIP1").isEmpty()) {
                userLevelRepository.save(UserLevel.builder()
                        .name("VIP1")
                        .numberOfCards(1)
                        .dailyTransactionAmount(5000000L) // 5 million
                        .build());
            }
            if (userLevelRepository.findByName("VIP2").isEmpty()) {
                userLevelRepository.save(UserLevel.builder()
                        .name("VIP2")
                        .numberOfCards(2)
                        .dailyTransactionAmount(10000000L) // 10 million
                        .build());
            }
            if (userLevelRepository.findByName("VIP3").isEmpty()) {
                userLevelRepository.save(UserLevel.builder()
                        .name("VIP3")
                        .numberOfCards(3)
                        .dailyTransactionAmount(20000000L) // 20 million
                        .build());
            }

            // Create account if admin doesn't exist
            if (accountRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
                // Create permissions
                Permission vip1 = permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.VIP1)
                        .description("VIP1")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.VIP2)
                        .description("VIP2")
                        .build());

                permissionRepository.save(Permission.builder()
                        .name(PredefinedPermission.VIP3)
                        .description("VIP3")
                        .build());

                var permissions = new HashSet<Permission>();
                permissions.add(vip1);

                // Create roles
                roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .permissions(permissions)
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                // Get VIP3 level for admin
                UserLevel adminUserLevel = userLevelRepository.findByName("VIP3")
                        .orElseThrow(() -> new RuntimeException("VIP3 user level not found"));

                // Create admin account
                Account account = Account.builder()
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .customerName(ADMIN_CUSTOMER_NAME)
                        .phoneNumber(ADMIN_PHONE_NUMBER)
                        .roles(roles)
                        .isActive(true)
                        .userLevel(adminUserLevel) // Assign VIP3 to admin
                        .build();

                accountRepository.save(account);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}