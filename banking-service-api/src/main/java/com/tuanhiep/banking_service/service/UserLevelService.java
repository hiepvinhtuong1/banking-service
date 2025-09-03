package com.tuanhiep.banking_service.service;

import com.tuanhiep.banking_service.dto.request.UserLevelCreationRequest;
import com.tuanhiep.banking_service.dto.response.RoleResponse;
import com.tuanhiep.banking_service.dto.response.UserLevelResponse;

import java.util.List;

public interface UserLevelService {

    List<UserLevelResponse> getAllUserLevels();

    UserLevelResponse createNew(UserLevelCreationRequest request);
}
