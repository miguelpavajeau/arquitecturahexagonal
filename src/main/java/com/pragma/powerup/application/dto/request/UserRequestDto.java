package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UserRequestDto {
    private String nombre;
    private String apellido;

    //El documento de identidad debe ser numerico.
    @NotNull(message = "El documento de identidad no puede estar vacio")
    //@NotBlank(message = "El documento de identidad no puede estar vacio")
    //@Pattern(regexp = "[0-9]+", message = "El documento de identidad debe ser numerico")
    private Long documentoIdentidad;
    @Size(max = 13, message = "El celular debe tener maximo 13 caracteres")
    private String celular;
    @Email(message = "El correo debe ser un correo valido", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotBlank(message = "El correo no puede estar vacio")
    private String correo;
    @NotNull(message = "La clave no puede estar vacia")
    private String clave;
    private Long idRol;
}