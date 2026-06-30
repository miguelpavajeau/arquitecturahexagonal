package com.pragma.powerup.infrastructure.exceptionhandler;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ControllerAdvisorTest {

    private final ControllerAdvisor controllerAdvisor = new ControllerAdvisor();

    @Test
    void genericException_returns500_withoutLeakingInternalMessage() {
        String internalDetail = "NullPointerException en línea 42: secreto interno";

        ResponseEntity<Map<String, Object>> response =
                controllerAdvisor.exception(new RuntimeException(internalDetail));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        Object errors = response.getBody().get("errors");
        assertNotNull(errors);
        // El mensaje interno NO debe filtrarse al cliente.
        assertFalse(errors.toString().contains(internalDetail));
        assertEquals("Ocurrió un error inesperado. Intenta nuevamente más tarde.", errors);
    }
}
