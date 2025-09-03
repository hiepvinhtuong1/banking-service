package com.tuanhiep.banking_service.controller;

import com.tuanhiep.banking_service.dto.request.UserLevelCreationRequest;
import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.dto.response.RoleResponse;
import com.tuanhiep.banking_service.dto.response.UserLevelResponse;
import com.tuanhiep.banking_service.service.RoleService;
import com.tuanhiep.banking_service.service.UserLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/userLevels")
public class UserLevelController {

    @Autowired
    private UserLevelService userLevelService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    APIResponse<List<UserLevelResponse>> getAllRoles() {
       return APIResponse.<List<UserLevelResponse>>builder()
               .data(userLevelService.getAllUserLevels())
               .build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    APIResponse<UserLevelResponse> createNewUserLevel(
            @RequestBody UserLevelCreationRequest request
    ) {
        return APIResponse.<UserLevelResponse>builder()
                .data(userLevelService.createNew(request))
                .build();
    }
}
