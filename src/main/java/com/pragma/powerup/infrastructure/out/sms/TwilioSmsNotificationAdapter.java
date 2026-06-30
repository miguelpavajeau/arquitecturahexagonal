package com.pragma.powerup.infrastructure.out.sms;

import com.pragma.powerup.domain.spi.ISmsNotificationPort;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Envía SMS reales a través de Twilio. Las credenciales se inyectan desde configuración
 * (variables de entorno) y nunca se versionan. Un fallo de envío se registra pero no se
 * propaga: la notificación es best-effort y no debe revertir el cambio de estado del pedido.
 */
public class TwilioSmsNotificationAdapter implements ISmsNotificationPort {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwilioSmsNotificationAdapter.class);

    private final String fromNumber;

    public TwilioSmsNotificationAdapter(String accountSid, String authToken, String fromNumber) {
        Twilio.init(accountSid, authToken);
        this.fromNumber = fromNumber;
    }

    @Override
    public void sendOrderReadySms(String celular, String pin) {
        try {
            Message message = Message.creator(
                    new PhoneNumber(celular),
                    new PhoneNumber(fromNumber),
                    "Tu pedido está listo. Presenta este PIN para reclamarlo: " + pin
            ).create();
            LOGGER.info("SMS enviado a {} (sid={})", celular, message.getSid());
        } catch (RuntimeException e) {
            LOGGER.error("No se pudo enviar el SMS a {}: {}", celular, e.getMessage());
        }
    }
}
