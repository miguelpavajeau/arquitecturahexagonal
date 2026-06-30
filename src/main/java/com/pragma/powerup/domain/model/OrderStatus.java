package com.pragma.powerup.domain.model;

/** Estados por los que transita un pedido. */
public enum OrderStatus {
    PENDIENTE,
    EN_PREPARACION,
    LISTO,
    ENTREGADO,
    CANCELADO
}
