package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class DeliverOrderRequestDto {

    @NotBlank(message = "El pin de seguridad es obligatorio")
    private String pin;
}
