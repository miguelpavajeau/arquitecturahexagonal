package com.pragma.powerup.application.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRequestDtoTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    @BeforeAll
    static void init() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    static void close() {
        factory.close();
    }

    private UserRequestDto validDto() {
        UserRequestDto dto = new UserRequestDto();
        dto.setNombre("Ana");
        dto.setApellido("Cliente");
        dto.setDocumentoIdentidad(123456789L);
        dto.setCelular("+573001234567");
        dto.setCorreo("ana@mail.com");
        dto.setClave("clave");
        return dto;
    }

    private boolean hasViolationOn(UserRequestDto dto, String field) {
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(dto);
        return violations.stream().anyMatch(v -> field.equals(v.getPropertyPath().toString()));
    }

    private boolean hasCelularViolation(UserRequestDto dto) {
        return hasViolationOn(dto, "celular");
    }

    @Test
    void should_Accept_Valid_Colombian_Cellphone() {
        assertTrue(validator.validate(validDto()).isEmpty());
    }

    @Test
    void should_Reject_Cellphone_Without_Country_Code() {
        UserRequestDto dto = validDto();
        dto.setCelular("3001234567");
        assertTrue(hasCelularViolation(dto));
    }

    @Test
    void should_Reject_Cellphone_With_Wrong_Country_Code() {
        UserRequestDto dto = validDto();
        dto.setCelular("+13001234567");
        assertTrue(hasCelularViolation(dto));
    }

    @Test
    void should_Reject_Cellphone_With_Wrong_Digit_Count() {
        UserRequestDto dto = validDto();
        dto.setCelular("+5730012345");
        assertTrue(hasCelularViolation(dto));
    }

    @Test
    void should_Reject_Cellphone_With_Non_Numeric_Characters() {
        UserRequestDto dto = validDto();
        dto.setCelular("+57300abc4567");
        assertTrue(hasCelularViolation(dto));
    }

    @Test
    void should_Reject_Blank_Cellphone() {
        UserRequestDto dto = validDto();
        dto.setCelular("");
        assertTrue(hasCelularViolation(dto));
    }

    @Test
    void should_Not_Report_Celular_When_Format_Is_Valid() {
        assertFalse(hasCelularViolation(validDto()));
    }

    @Test
    void should_Reject_Null_Document() {
        UserRequestDto dto = validDto();
        dto.setDocumentoIdentidad(null);
        assertTrue(hasViolationOn(dto, "documentoIdentidad"));
    }

    @Test
    void should_Reject_Zero_Document() {
        UserRequestDto dto = validDto();
        dto.setDocumentoIdentidad(0L);
        assertTrue(hasViolationOn(dto, "documentoIdentidad"));
    }

    @Test
    void should_Reject_Negative_Document() {
        UserRequestDto dto = validDto();
        dto.setDocumentoIdentidad(-5L);
        assertTrue(hasViolationOn(dto, "documentoIdentidad"));
    }

    @Test
    void should_Accept_Positive_Document() {
        assertFalse(hasViolationOn(validDto(), "documentoIdentidad"));
    }
}
