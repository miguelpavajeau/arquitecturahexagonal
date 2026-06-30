package com.pragma.powerup.application.dto.request;

import com.pragma.powerup.infrastructure.configuration.Constants;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

import static com.pragma.powerup.infrastructure.configuration.Constants.*;

@Getter
@Setter
public class UserRequestDto {
    @NotBlank(message = NOMBRE)
    private String nombre;
    @NotBlank(message = APELLIDO)
    private String apellido;
    @NotNull(message = DOCUMENTO_IDENTIDAD)
    @Positive(message = DOCUMENTO_IDENTIDAD_POSITIVO)
    private Long documentoIdentidad;
    @NotBlank(message = CELULAR)
    @Pattern(regexp = Constants.EXPRESION_REGULAR_CELULAR, message = CELULAR)
    private String celular;
    @Email(message = CORREO, regexp = Constants.EXPRESION_REGULAR_CORREO)
    @NotBlank(message = CORREO_VACIO)
    private String correo;
    @NotBlank(message = CLAVE)
    private String clave;
}
