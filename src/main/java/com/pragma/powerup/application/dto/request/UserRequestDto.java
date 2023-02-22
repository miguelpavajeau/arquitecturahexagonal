package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class UserRequestDto {
    private String nombre;
    private String apellido;
    private Long documentoIdentidad;
    private String celular;
    @Email
    private String correo;
    private String clave;
    private Long idRol;
}