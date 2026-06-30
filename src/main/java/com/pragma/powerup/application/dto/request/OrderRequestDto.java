package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    @NotNull(message = "El restaurante es obligatorio")
    private Long idRestaurante;

    @NotEmpty(message = "El pedido debe contener al menos un plato")
    @Valid
    private List<OrderDishRequestDto> platos;
}
