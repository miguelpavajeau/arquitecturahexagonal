package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {
    private String token;
    private String tokenType = "Bearer";

    public LoginResponseDto(String token) {
        this.token = token;
    }
}
