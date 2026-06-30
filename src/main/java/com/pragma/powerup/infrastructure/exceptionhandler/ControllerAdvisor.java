package com.pragma.powerup.infrastructure.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pragma.powerup.domain.exception.AuthenticationFailedException;
import com.pragma.powerup.domain.exception.CorreoYaExisteException;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.exception.EntityNotFoundException;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAdvisor.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> err.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errors);
        }
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> invalidFormatException(InvalidFormatException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errors", "Error de formato");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, Object>> noDataFoundException(NoDataFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errors", "No se encontraron datos");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CorreoYaExisteException.class)
    public ResponseEntity<Map<String, Object>> correoYaExisteException(CorreoYaExisteException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errors", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Map<String, Object>> authenticationFailedException(AuthenticationFailedException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errors", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, Object>> entityNotFoundException(EntityNotFoundException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errors", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<Map<String, Object>> domainException(DomainException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errors", e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> accessDeniedException(AccessDeniedException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("errors", "No tiene permisos para realizar esta acción");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> exception(Exception e) {
        LOGGER.error("Error inesperado procesando la solicitud", e);
        Map<String, Object> response = new HashMap<>();
        response.put("errors", "Ocurrió un error inesperado. Intenta nuevamente más tarde.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
