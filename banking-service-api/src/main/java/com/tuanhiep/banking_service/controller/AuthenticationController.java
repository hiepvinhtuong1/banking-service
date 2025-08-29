package com.tuanhiep.banking_service.controller;

import com.nimbusds.jose.JOSEException;
import com.tuanhiep.banking_service.dto.request.*;
import com.tuanhiep.banking_service.dto.response.*;
import com.tuanhiep.banking_service.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping("/login")
    APIResponse<LoginResponse> login (@RequestBody @Valid LoginRequest request) {
        LoginResponse result = authenticationService.login(request);
        return APIResponse.<LoginResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/register")
    APIResponse<AccountResponse> register (@RequestBody @Valid AccountCreationRequest request) {
        AccountResponse result = authenticationService.register(request);
        return APIResponse.<AccountResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/logout")
    APIResponse<Void> logout(@RequestBody LogoutRequest request)  throws ParseException, JOSEException {
        authenticationService.logout(request);
        return APIResponse.<Void>builder().build();
    }

    @PostMapping("/refresh-token")
    APIResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest request) throws ParseException, JOSEException {
        var result = authenticationService.refreshToken(request);
        return APIResponse.<RefreshTokenResponse>builder()
                .data(result)
                .build();
    }

    @PostMapping("/verification")
    APIResponse<AccountResponse> verifyAccount(
            @RequestParam(required = true) String email,
            @RequestParam(required = true) String code
    ) {
        var result = authenticationService.verifyAccount(email, code);
        return APIResponse.<AccountResponse>builder()
                .data(result)
                .build();
    }
}
