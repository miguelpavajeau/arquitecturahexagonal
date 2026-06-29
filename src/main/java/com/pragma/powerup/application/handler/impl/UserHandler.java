package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.UserRequestDto;
import com.pragma.powerup.application.dto.response.UserResponseDto;
import com.pragma.powerup.application.handler.IUserHandler;
import com.pragma.powerup.application.mapper.IUserRequestMapper;
import com.pragma.powerup.application.mapper.IUserResponseMapper;
import com.pragma.powerup.domain.api.IUserServicePort;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.util.IUserPasswordEncrypt;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;
    private final IUserResponseMapper userResponseMapper;
    private final IUserPasswordEncrypt userPasswordEncrypt;

    @Override
    public void saveUser(UserRequestDto userRequestDto) {
        userServicePort.saveUser(toEncryptedUser(userRequestDto));
    }

    @Override
    public void saveEmployee(UserRequestDto userRequestDto) {
        String correoPropietario = SecurityContextHolder.getContext().getAuthentication().getName();
        userServicePort.saveEmployee(toEncryptedUser(userRequestDto), correoPropietario);
    }

    @Override
    public void saveClient(UserRequestDto userRequestDto) {
        userServicePort.saveClient(toEncryptedUser(userRequestDto));
    }

    private UserModel toEncryptedUser(UserRequestDto userRequestDto) {
        UserModel userModel = userRequestMapper.toUser(userRequestDto);
        userModel.setClave(userPasswordEncrypt.encryptPassword(userRequestDto.getClave()));
        return userModel;
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userResponseMapper.toResponseList(userServicePort.getAllUsers());
    }
}
