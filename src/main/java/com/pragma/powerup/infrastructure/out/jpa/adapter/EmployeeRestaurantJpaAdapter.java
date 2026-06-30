package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.EmployeeRestaurantModel;
import com.pragma.powerup.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.EmployeeRestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.repository.IEmployeeRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmployeeRestaurantJpaAdapter implements IEmployeeRestaurantPersistencePort {

    private final IEmployeeRestaurantRepository employeeRestaurantRepository;

    @Override
    public EmployeeRestaurantModel save(EmployeeRestaurantModel model) {
        EmployeeRestaurantEntity entity = new EmployeeRestaurantEntity(
                model.getId(), model.getIdEmpleado(), model.getIdRestaurante());
        EmployeeRestaurantEntity saved = employeeRestaurantRepository.save(entity);
        return new EmployeeRestaurantModel(saved.getId(), saved.getIdEmpleado(), saved.getIdRestaurante());
    }

    @Override
    public Long findRestaurantIdByEmployeeId(Long idEmpleado) {
        EmployeeRestaurantEntity entity = employeeRestaurantRepository.findByIdEmpleado(idEmpleado);
        return entity == null ? null : entity.getIdRestaurante();
    }
}
