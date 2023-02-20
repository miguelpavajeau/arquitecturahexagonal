package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObjectRequestDto {
    private String nombre;
    private String apellido;
    private String documento_identidad;
    private int celular;
    private String correo;
    private String clave;
    private Long id_rol;
}
