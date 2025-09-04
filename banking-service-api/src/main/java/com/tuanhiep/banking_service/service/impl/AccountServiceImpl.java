package com.tuanhiep.banking_service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.dto.request.AccountUpdateRequest;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.entity.Account;
import com.tuanhiep.banking_service.entity.Balance;
import com.tuanhiep.banking_service.entity.Role;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.mapper.AccountMapper;
import com.tuanhiep.banking_service.repository.AccountRepository;
import com.tuanhiep.banking_service.repository.BalanceRepository;
import com.tuanhiep.banking_service.repository.RoleRepository;
import com.tuanhiep.banking_service.repository.UserLeveLRepository;
import com.tuanhiep.banking_service.service.AccountService;
import com.tuanhiep.banking_service.service.impl.specification.AccountSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserLeveLRepository userLeveLRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ACCOUNT_KEY_PREFIX = "account:";


    @Override
    public AccountResponse createNew(AccountCreationRequest request) {

        if (accountRepository.existsAccountByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.ACCOUNT_PHONE_NUMBER_EXISTED);
        }

        Account newAccount = accountMapper.toAccount(request);
        newAccount.setCustomerName(request.getEmail().split("@")[0]);

        // UserLevel
        Integer userLevelId = request.getUserLevelId();
        if (userLevelId == null) {
            userLevelId = 1; // mặc định
        } else if (!userLeveLRepository.existsById(userLevelId)) {
            throw new AppException(ErrorCode.USER_LEVEL_NOT_FOUND);
        }
        newAccount.setUserLevel(userLeveLRepository.findById(userLevelId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_LEVEL_NOT_FOUND)));

        // Role
        String roleName = request.getRoleName();
        if (!StringUtils.hasText(roleName)) {
            roleName = "USER"; // mặc định
        } else if (!roleRepository.existsById(roleName)) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }

        Role role = roleRepository.findById(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        HashSet<Role> roles = new HashSet<>();
        roles.add(role);
        newAccount.setRoles(roles);

        // Password
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        newAccount.setPassword(passwordEncoder.encode(request.getPassword()));

        newAccount.setVerifyCode(null);
        newAccount.setActive(true);
        newAccount.setDestroy(false);

        // 👉 Save Account trước
        Account savedAccount = accountRepository.save(newAccount);

        // 👉 Khởi tạo Balance mặc định
        Balance balance = new Balance();
        balance.setAvailableBalance(BigDecimal.ZERO);
        balance.setHoldBalance(BigDecimal.ZERO);
        balance.setCreatedAt(LocalDateTime.now());
        balance.setUpdatedAt(LocalDateTime.now());
        balance.setAccount(savedAccount);

        balanceRepository.save(balance);

        // 👉 Gắn balance lại vào account để mapping đầy đủ
        savedAccount.setBalance(balance);

        return accountMapper.toAccountResponse(savedAccount);
    }



    @Override
    public AccountResponse getAccount(String accountId) {
        String key = ACCOUNT_KEY_PREFIX + accountId;

        // 1. Kiểm tra Redis
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            AccountResponse response = objectMapper.convertValue(cached, AccountResponse.class);
            System.out.println("👉 Lấy account từ Redis Cache");
            return response;
        }

        // 2. Nếu chưa có thì lấy DB
        AccountResponse response = accountMapper.toAccountResponse(
                accountRepository.findById(accountId).orElseThrow(
                        () -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND)));

        // 3. Lưu vào Redis (TTL 10 phút)
        redisTemplate.opsForValue().set(key, response, 10, TimeUnit.MINUTES);

        return response;
    }

    @Override
    public Page<AccountResponse> getAllAccounts(Pageable pageable, String customerName, String phoneNumber, String email) {
        return accountRepository.findAll(AccountSpecification.combineFilters(customerName, phoneNumber, email), pageable)
                .map(accountMapper::toAccountResponse);
    }

    @Override
    public AccountResponse updateAccount(String accountId, AccountUpdateRequest request) {
        Account existedAccount = accountRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_FOUND));

        if (!existedAccount.getPhoneNumber().equals(request.getPhoneNumber()) &&
                accountRepository.existsAccountByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.ACCOUNT_PHONE_NUMBER_EXISTED);
        }

        existedAccount.setPhoneNumber(request.getPhoneNumber());
        existedAccount.setUserLevel(userLeveLRepository.findById(request.getLevel())
                .orElseThrow(() -> new AppException(ErrorCode.USER_LEVEL_NOT_FOUND)));

        existedAccount.getRoles().clear();
        Role newRole = roleRepository.findById(request.getRole())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        existedAccount.getRoles().add(newRole);

        existedAccount.setCustomerName(request.getCustomerName());

        AccountResponse updated = accountMapper.toAccountResponse(accountRepository.save(existedAccount));

        // Xoá cache sau khi update
        String key = ACCOUNT_KEY_PREFIX + accountId;
        redisTemplate.delete(key);

        return updated;
    }


}
