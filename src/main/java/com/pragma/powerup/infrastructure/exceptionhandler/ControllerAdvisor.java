package com.pragma.powerup.infrastructure.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pragma.powerup.infrastructure.exception.NoDataFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.pragma.powerup.infrastructure.configuration.Constants.CORREO_EXISTENTE;

/**
 * @author Miguel Pavajeau
 * @version 1.0
 * @since 1.0
 */

@ControllerAdvice
public class ControllerAdvisor {

    private static final String MESSAGE = "message";

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
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<Map<String, Object>> invalidFormatException(InvalidFormatException e) {

        InvalidFormatException result = (InvalidFormatException) e;
        Map<String, Object> response = new HashMap<>();

        response.put("errors", "Error de formato");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Map<String, Object>> noDataFoundException(NoDataFoundException e) {

        NoDataFoundException result = (NoDataFoundException) e;
        Map<String, Object> response = new HashMap<>();

        response.put("errors", "No se encontraron datos");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> exception(Exception e) {

        Exception result = (Exception) e;
        Map<String, Object> response = new HashMap<>();

        response.put("errors", CORREO_EXISTENTE);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<Map<String, Object>> httpMessageNotReadableException(HttpMessageNotReadableException e) {
//
//        HttpMessageNotReadableException result = (HttpMessageNotReadableException) e.getHttpInputMessage();
//        Map<String, Object> response = new HashMap<>();
//
//        response.put("errors", "Error de lectura");
//
//        if (result.hasErrors()) {
//            List<String> errors = result.getFieldErrors()
//                    .stream()
//                    .map(err -> err.getDefaultMessage())
//                    .collect(Collectors.toList());
//            response.put("errors", errors);
//        }
//        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
//    }
}