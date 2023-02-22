package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.RoleRequestDto;
import com.pragma.powerup.application.dto.response.RoleResponseDto;
import com.pragma.powerup.application.handler.IRoleHandler;
import com.pragma.powerup.application.mapper.IRoleRequestMapper;
import com.pragma.powerup.application.mapper.IRoleResponseMapper;
import com.pragma.powerup.domain.api.IRoleServicePort;
import com.pragma.powerup.domain.model.RoleModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleHandler implements IRoleHandler {

    private final IRoleServicePort roleServicePort;
    private final IRoleRequestMapper roleRequestMapper;
    private final IRoleResponseMapper roleResponseMapper;

    @Override
    public void saveRole(RoleRequestDto roleRequestDto) {
        RoleModel roleModel = roleRequestMapper.toRole(roleRequestDto);
        roleServicePort.saveRole(roleModel);
    }
    @Override
    public List<RoleResponseDto> getAllRoles() {
        return roleResponseMapper.toResponseList(roleServicePort.getAllRoles());
    }
}