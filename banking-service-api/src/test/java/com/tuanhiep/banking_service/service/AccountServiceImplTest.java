package com.tuanhiep.banking_service.service;


import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.entity.*;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.mapper.AccountMapper;
import com.tuanhiep.banking_service.repository.*;
import com.tuanhiep.banking_service.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceImplTest {

    @Mock private AccountRepository accountRepository;
    @Mock private UserLeveLRepository userLeveLRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private BalanceRepository balanceRepository;
    @Mock private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private AccountCreationRequest buildRequest() {
        return AccountCreationRequest.builder()
                .email("test@example.com")
                .phoneNumber("0123456789")
                .password("password1")
                .userLevelId(1)
                .roleName("USER")
                .build();
    }

    @Test
    void createNew_ShouldThrow_WhenPhoneNumberExists() {
        var request = buildRequest();
        when(accountRepository.existsAccountByPhoneNumber(request.getPhoneNumber()))
                .thenReturn(true);

        Throwable thrown = catchThrowable(() -> accountService.createNew(request));

        assertThat(thrown).isInstanceOf(AppException.class);
        AppException ex = (AppException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ACCOUNT_PHONE_NUMBER_EXISTED);

        verify(accountRepository, never()).save(any());
    }

    @Test
    void createNew_ShouldThrow_WhenUserLevelNotFound() {
        var request = buildRequest();
        when(accountRepository.existsAccountByPhoneNumber(anyString())).thenReturn(false);
        when(accountMapper.toAccount(request)).thenReturn(new Account());
        when(userLeveLRepository.existsById(1)).thenReturn(false);

        Throwable thrown = catchThrowable(() -> accountService.createNew(request));

        assertThat(thrown).isInstanceOf(AppException.class);
        AppException ex = (AppException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_LEVEL_NOT_FOUND);
    }

    @Test
    void createNew_ShouldThrow_WhenRoleNotFound() {
        var request = buildRequest();
        when(accountRepository.existsAccountByPhoneNumber(anyString())).thenReturn(false);
        when(accountMapper.toAccount(request)).thenReturn(new Account());
        when(userLeveLRepository.existsById(1)).thenReturn(true);
        when(userLeveLRepository.findById(1)).thenReturn(Optional.of(new UserLevel()));

        when(roleRepository.existsById("USER")).thenReturn(false);

        Throwable thrown = catchThrowable(() -> accountService.createNew(request));

        assertThat(thrown).isInstanceOf(AppException.class);
        AppException ex = (AppException) thrown;
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.ROLE_NOT_FOUND);
    }


    @Test
    void createNew_ShouldSuccess_WhenValidRequest() {
        var request = buildRequest();

        mockCommonValidations(request, 1, "USER");

        AccountResponse expectedResponse = AccountResponse.builder()
                .accountId("ACC123")
                .email(request.getEmail())
                .active(true)
                .build();
        when(accountMapper.toAccountResponse(any(Account.class))).thenReturn(expectedResponse);

        AccountResponse actual = accountService.createNew(request);

        assertThat(actual.getAccountId()).isEqualTo("ACC123");
        assertThat(actual.getEmail()).isEqualTo("test@example.com");
        assertThat(actual.isActive()).isTrue();

        verify(accountRepository).save(any(Account.class));
        verify(balanceRepository).save(any(Balance.class));
    }

    @Test
    void createNew_ShouldUseDefaultUserLevel_WhenUserLevelIdIsNull() {
        var request = buildRequest();
        request.setUserLevelId(null); // bỏ trống

        mockCommonValidations(request, 1, "USER");

        AccountResponse expectedResponse = AccountResponse.builder()
                .accountId("ACC456")
                .email(request.getEmail())
                .active(true)
                .build();
        when(accountMapper.toAccountResponse(any(Account.class))).thenReturn(expectedResponse);

        AccountResponse actual = accountService.createNew(request);

        assertThat(actual.getAccountId()).isEqualTo("ACC456");
        verify(userLeveLRepository).findById(1); // ✅ đúng
        verify(userLeveLRepository, never()).existsById(1); // ❌ không gọi
    }


    @Test
    void createNew_ShouldUseDefaultRole_WhenRoleNameIsNullOrBlank() {
        var request = buildRequest();
        request.setRoleName(""); // để trống

        mockCommonValidations(request, 1, "USER");

        AccountResponse expectedResponse = AccountResponse.builder()
                .accountId("ACC789")
                .email(request.getEmail())
                .active(true)
                .build();
        when(accountMapper.toAccountResponse(any(Account.class))).thenReturn(expectedResponse);

        AccountResponse actual = accountService.createNew(request);

        assertThat(actual.getAccountId()).isEqualTo("ACC789");

        // ✅ vì branch "roleName trống" nên chỉ gọi findById, không gọi existsById
        verify(roleRepository).findById("USER");
        verify(roleRepository, never()).existsById("USER");
    }


    /**
     * Mock cho các trường hợp valid (tránh lặp code).
     */
    private void mockCommonValidations(AccountCreationRequest request, int userLevelId, String roleName) {
        when(accountRepository.existsAccountByPhoneNumber(anyString())).thenReturn(false);

        UserLevel userLevel = new UserLevel();
        userLevel.setId(userLevelId);
        when(userLeveLRepository.existsById(userLevelId)).thenReturn(true);
        when(userLeveLRepository.findById(userLevelId)).thenReturn(Optional.of(userLevel));

        Role role = new Role();
        role.setName(roleName);
        when(roleRepository.existsById(roleName)).thenReturn(true);
        when(roleRepository.findById(roleName)).thenReturn(Optional.of(role));

        Account accountEntity = new Account();
        accountEntity.setEmail(request.getEmail());
        when(accountMapper.toAccount(request)).thenReturn(accountEntity);

        Account savedAccount = new Account();
        savedAccount.setAccountId("ACC-" + UUID.randomUUID());
        savedAccount.setEmail(request.getEmail());
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        Balance balance = new Balance();
        balance.setAvailableBalance(BigDecimal.ZERO);
        when(balanceRepository.save(any(Balance.class))).thenReturn(balance);
    }
}
