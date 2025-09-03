package com.tuanhiep.banking_service.mapper;

import com.tuanhiep.banking_service.dto.request.UserLevelCreationRequest;
import com.tuanhiep.banking_service.dto.response.UserLevelResponse;
import com.tuanhiep.banking_service.entity.UserLevel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserLevelMapper {
    UserLevelResponse toUserLevelResponse(UserLevel userLevel);

    UserLevel toUserLevel(UserLevelCreationRequest request);
}
