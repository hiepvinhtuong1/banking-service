package com.tuanhiep.banking_service.service.impl;

import com.tuanhiep.banking_service.dto.request.UserLevelCreationRequest;
import com.tuanhiep.banking_service.dto.response.RoleResponse;
import com.tuanhiep.banking_service.dto.response.UserLevelResponse;
import com.tuanhiep.banking_service.entity.UserLevel;
import com.tuanhiep.banking_service.exception.AppException;
import com.tuanhiep.banking_service.exception.ErrorCode;
import com.tuanhiep.banking_service.mapper.RoleMapper;
import com.tuanhiep.banking_service.mapper.UserLevelMapper;
import com.tuanhiep.banking_service.repository.RoleRepository;
import com.tuanhiep.banking_service.repository.UserLeveLRepository;
import com.tuanhiep.banking_service.service.RoleService;
import com.tuanhiep.banking_service.service.UserLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserLevelServiceImpl implements UserLevelService {

    @Autowired
    private UserLeveLRepository userLeveLRepository;

    @Autowired
    private UserLevelMapper userLevelMapper;



    @Override
    public List<UserLevelResponse> getAllUserLevels() {
        return userLeveLRepository.findAll()
                .stream()
                .map(userLevelMapper::toUserLevelResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserLevelResponse createNew(UserLevelCreationRequest request) {
        // check trùng tên level
        if (userLeveLRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.USER_LEVEL_DUPLICATED);
        }

        // map request -> entity
        UserLevel userLevel = userLevelMapper.toUserLevel(request);

        // save vào DB
        UserLevel saved = userLeveLRepository.save(userLevel);

        // trả về response
        return userLevelMapper.toUserLevelResponse(saved);
    }
}
