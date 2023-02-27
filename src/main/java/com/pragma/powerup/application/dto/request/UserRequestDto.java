package com.pragma.powerup.application.dto.request;

import com.pragma.powerup.infrastructure.configuration.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.pragma.powerup.infrastructure.configuration.Constants.*;

@Getter
@Setter
public class UserRequestDto {
    private String nombre;
    private String apellido;

    @NotNull(message = DOCUMENTO_IDENTIDAD)
    private Long documentoIdentidad;
    @Size(max = 13, message = CELULAR)
    private String celular;
    @Email(message = CORREO, regexp = Constants.EXPRESION_REGULAR_CORREO)
    @NotBlank(message = CORREO_VACIO)
    private String correo;
    @NotNull(message = CLAVE)
    private String clave;
    private Long idRol;
}