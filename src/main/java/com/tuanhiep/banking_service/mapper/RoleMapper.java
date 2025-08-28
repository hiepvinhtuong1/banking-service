package com.tuanhiep.banking_service.mapper;

import com.tuanhiep.banking_service.dto.response.RoleResponse;
import com.tuanhiep.banking_service.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
}
