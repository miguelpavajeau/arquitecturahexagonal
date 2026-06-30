package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import static com.pragma.powerup.infrastructure.configuration.Constants.PLATO_DESCRIPCION;
import static com.pragma.powerup.infrastructure.configuration.Constants.PLATO_PRECIO;

@Getter
@Setter
public class DishUpdateRequestDto {
    @NotNull(message = PLATO_PRECIO)
    @Positive(message = PLATO_PRECIO)
    private Integer precio;
    @NotBlank(message = PLATO_DESCRIPCION)
    private String descripcion;
}
