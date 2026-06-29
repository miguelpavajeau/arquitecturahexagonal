package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.LoginRequestDto;
import com.pragma.powerup.application.dto.response.LoginResponseDto;
import com.pragma.powerup.application.handler.IAuthHandler;
import com.pragma.powerup.domain.api.IAuthenticationServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthHandler implements IAuthHandler {

    private final IAuthenticationServicePort authenticationServicePort;

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String token = authenticationServicePort.login(loginRequestDto.getCorreo(), loginRequestDto.getClave());
        return new LoginResponseDto(token);
    }
}
