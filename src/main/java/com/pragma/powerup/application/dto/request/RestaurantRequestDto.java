package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import static com.pragma.powerup.infrastructure.configuration.Constants.*;

@Getter
@Setter
public class RestaurantRequestDto {
    @NotBlank(message = RESTAURANTE_NOMBRE)
    private String nombre;
    @NotNull(message = RESTAURANTE_NIT)
    private Long nit;
    @NotBlank(message = RESTAURANTE_DIRECCION)
    private String direccion;
    @NotBlank(message = RESTAURANTE_TELEFONO)
    @Pattern(regexp = EXPRESION_REGULAR_TELEFONO, message = RESTAURANTE_TELEFONO)
    private String telefono;
    @NotBlank(message = RESTAURANTE_URL_LOGO)
    private String urlLogo;
    @NotNull(message = RESTAURANTE_ID_PROPIETARIO)
    private Long idPropietario;
}
