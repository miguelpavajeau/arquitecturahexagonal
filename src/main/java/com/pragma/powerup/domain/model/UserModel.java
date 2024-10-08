package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class UserModel {
    private Long id;
    private String nombre;
    private String apellido;
    private Long documentoIdentidad;
    private String celular;
    private String correo;
    private String clave;
    //private Long idRol;
}