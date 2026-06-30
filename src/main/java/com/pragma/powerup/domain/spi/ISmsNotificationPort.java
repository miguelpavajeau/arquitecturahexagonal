package com.pragma.powerup.domain.spi;

public interface ISmsNotificationPort {

    /** Envía al cliente un SMS avisando que su pedido está listo, con el pin de reclamo. */
    void sendOrderReadySms(String celular, String pin);
}
