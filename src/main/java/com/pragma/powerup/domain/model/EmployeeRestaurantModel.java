package com.pragma.powerup.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Relación que indica a qué restaurante pertenece un empleado. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeRestaurantModel {
    private Long id;
    private Long idEmpleado;
    private Long idRestaurante;
}
