package com.pragma.powerup.domain.exception;

public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public static final String ERROR_FORMAT_DATE = "No fue posible asignar el formato dado: ";

    public static final String ERROR_NULL = "No ES POSIBLE QUE EL VALOR SEA NULO";

//    public ExcepcionFecha(String message) {
//        super(message);
//    }
}