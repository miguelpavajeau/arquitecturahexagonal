package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.EmployeeRestaurantModel;

public interface IEmployeeRestaurantPersistencePort {

    EmployeeRestaurantModel save(EmployeeRestaurantModel employeeRestaurantModel);

    /** Restaurante al que pertenece el empleado, o null si no tiene asignación. */
    Long findRestaurantIdByEmployeeId(Long idEmpleado);
}
