package com.pragma.powerup.domain.exception;

public class CorreoYaExisteException extends DomainException {
    public CorreoYaExisteException() {
        super("El correo ya existe");
    }
}
