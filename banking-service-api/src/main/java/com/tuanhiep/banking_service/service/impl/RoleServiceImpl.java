package com.tuanhiep.banking_service.service.impl;

import com.tuanhiep.banking_service.dto.response.RoleResponse;
import com.tuanhiep.banking_service.mapper.RoleMapper;
import com.tuanhiep.banking_service.repository.RoleRepository;
import com.tuanhiep.banking_service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Override
    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .collect(Collectors.toList());
    }

}
