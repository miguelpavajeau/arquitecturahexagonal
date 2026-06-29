package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

/** Vista de cliente: nombre y logo del restaurante (id para navegar a su menú). */
@Getter
@Setter
public class RestaurantListResponseDto {
    private Long id;
    private String nombre;
    private String urlLogo;
}
