package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Línea de un pedido: un plato y la cantidad solicitada. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDishModel {
    private Long id;
    private Long idPlato;
    private Integer cantidad;
}
