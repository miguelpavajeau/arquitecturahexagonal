package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private Long idCliente;
    private Long idRestaurante;
    private LocalDateTime fecha;
    private String estado;
    private Long idChef;
    private String pin;
    private List<OrderDishResponseDto> platos;
}
