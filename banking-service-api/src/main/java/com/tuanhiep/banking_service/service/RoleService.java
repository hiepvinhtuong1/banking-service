package com.tuanhiep.banking_service.service;

import com.tuanhiep.banking_service.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {

    List<RoleResponse> getAllRoles();
}
