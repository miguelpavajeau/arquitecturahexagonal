package com.pragma.powerup.infrastructure.configuration;

import javax.validation.constraints.NotNull;

import static com.pragma.powerup.infrastructure.configuration.Constants.CLAVE;

public class BCryptPasswordEncoder {
    public static String encode(String password) {
        return password;
    }

    public boolean matches(String clave, @NotNull(message = CLAVE) String s) {
        return false;
    }
}