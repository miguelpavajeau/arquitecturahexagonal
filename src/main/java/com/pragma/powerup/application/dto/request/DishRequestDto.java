package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.pragma.powerup.infrastructure.configuration.Constants.*;

@Getter
@Setter
public class DishRequestDto {
    @NotBlank(message = PLATO_NOMBRE)
    private String nombre;
    @NotNull(message = PLATO_PRECIO)
    @Positive(message = PLATO_PRECIO)
    private Integer precio;
    @NotBlank(message = PLATO_DESCRIPCION)
    private String descripcion;
    @NotBlank(message = PLATO_URL_IMAGEN)
    private String urlImagen;
    @NotNull(message = PLATO_ID_CATEGORIA)
    private Long idCategoria;
    @NotNull(message = PLATO_ID_RESTAURANTE)
    private Long idRestaurante;
}
