package com.tuanhiep.banking_service.mapper;

import com.tuanhiep.banking_service.dto.response.PermissionResponse;
import com.tuanhiep.banking_service.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionResponse toPermissionResponse(Permission permission);
}
