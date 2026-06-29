package com.pragma.powerup.infrastructure.out.sms;

import com.pragma.powerup.domain.spi.ISmsNotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación de notificación por SMS. En esta versión registra el mensaje en el log
 * (sustituible por una integración real, p. ej. Twilio, sin tocar el dominio).
 */
public class SmsNotificationAdapter implements ISmsNotificationPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotificationAdapter.class);

    @Override
    public void sendOrderReadySms(String celular, String pin) {
        LOGGER.info("SMS -> {} | Tu pedido está listo. Presenta este PIN para reclamarlo: {}", celular, pin);
    }
}
