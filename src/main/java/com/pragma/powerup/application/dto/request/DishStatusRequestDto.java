package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
public class DishStatusRequestDto {
    @NotNull(message = "El estado del plato es obligatorio")
    private Boolean activo;
}
