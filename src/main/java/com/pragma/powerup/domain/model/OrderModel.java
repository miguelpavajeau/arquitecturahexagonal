package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/** Pedido realizado por un cliente sobre los platos de un restaurante. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {
    private Long id;
    private Long idCliente;
    private Long idRestaurante;
    private LocalDateTime fecha;
    private OrderStatus estado;
    private Long idChef;
    private String pin;
    private List<OrderDishModel> platos;
}
