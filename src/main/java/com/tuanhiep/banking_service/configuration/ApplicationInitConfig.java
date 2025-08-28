package com.tuanhiep.banking_service.configuration;
import java.util.HashSet;

import com.tuanhiep.banking_service.constant.PredefinedPermission;
import com.tuanhiep.banking_service.constant.PredefinedRole;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.entity.Permission;
import com.tuanhiep.banking_service.entity.Role;
import com.tuanhiep.banking_service.repository.AccountRepository;
import com.tuanhiep.banking_service.repository.PermissionRepository;
import com.tuanhiep.banking_service.repository.RoleRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
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
    ApplicationRunner applicationRunner(AccountRepository accountRepository, RoleRepository roleRepository, PermissionRepository permissionRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (accountRepository.findByEmail(ADMIN_EMAIL).isEmpty()) {
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

                var permissons = new HashSet<Permission>();
                permissons.add(vip1);

                roleRepository.save(Role.builder()
                        .name(PredefinedRole.USER_ROLE)
                        .description("User role")
                        .permissions(permissons)
                        .build());

                Role adminRole = roleRepository.save(Role.builder()
                        .name(PredefinedRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(adminRole);

                Account account = Account.builder()
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .customerName(ADMIN_CUSTOMER_NAME)
                        .phoneNumber(ADMIN_PHONE_NUMBER)
                        .roles(roles)
                        .build();

                accountRepository.save(account);
                log.warn("admin user has been created with default password: admin, please change it");
            }
            log.info("Application initialization completed .....");
        };
    }
}
