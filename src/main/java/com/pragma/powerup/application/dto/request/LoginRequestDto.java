package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginRequestDto {
    @NotBlank(message = "El correo no puede estar vacio")
    private String correo;
    @NotBlank(message = "La clave no puede estar vacia")
    private String clave;
}
