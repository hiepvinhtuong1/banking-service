package com.tuanhiep.banking_service.controller;

import com.tuanhiep.banking_service.dto.request.LoginRequest;
import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.dto.response.LoginResponse;
import com.tuanhiep.banking_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    @Autowired
    AuthenticationService authenticationService;
    @PostMapping("/login")
    APIResponse<LoginResponse> login (@RequestBody LoginRequest request) {
        LoginResponse result = authenticationService.login(request);
        return APIResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }
}
