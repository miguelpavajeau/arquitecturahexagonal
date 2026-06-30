package com.pragma.powerup.domain.exception;

public class AuthenticationFailedException extends DomainException {
    public AuthenticationFailedException() {
        super("Usuario o clave incorrectos");
    }
}
