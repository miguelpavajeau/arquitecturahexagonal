package com.pragma.powerup.application.dto.response;

import lombok.Getter;
import lombok.Setter;

/** Vista de cliente: datos del plato activo junto a su categoría. */
@Getter
@Setter
public class DishListResponseDto {
    private Long id;
    private String nombre;
    private Integer precio;
    private String descripcion;
    private String urlImagen;
    private String categoria;
}
