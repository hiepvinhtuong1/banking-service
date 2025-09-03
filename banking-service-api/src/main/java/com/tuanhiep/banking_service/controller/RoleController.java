package com.tuanhiep.banking_service.controller;

import com.tuanhiep.banking_service.dto.request.AccountCreationRequest;
import com.tuanhiep.banking_service.dto.response.APIResponse;
import com.tuanhiep.banking_service.dto.response.AccountResponse;
import com.tuanhiep.banking_service.dto.response.RoleResponse;
import com.tuanhiep.banking_service.service.RoleService;
import com.tuanhiep.banking_service.service.impl.AccountServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    APIResponse<List<RoleResponse>> getAllRoles() {
       return APIResponse.<List<RoleResponse>>builder()
               .data(roleService.getAllRoles())
               .build();
    }
}
