package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.PagedResult;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

import java.util.List;

public class RestaurantUseCase implements IRestaurantServicePort {

    private static final String ROL_PROPIETARIO = "PROPIETARIO";
    private static final String SOLO_NUMEROS_REGEX = "^\\d+$";

    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserPersistencePort userPersistencePort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistencePort, IUserPersistencePort userPersistencePort) {
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public void saveRestaurant(RestaurantModel restaurantModel) {
        if (restaurantModel.getNombre() == null || restaurantModel.getNombre().isBlank()) {
            throw new DomainException("El nombre del restaurante es obligatorio");
        }
        if (restaurantModel.getNombre().matches(SOLO_NUMEROS_REGEX)) {
            throw new DomainException("El nombre del restaurante no puede contener solo numeros");
        }
        if (restaurantModel.getNit() == null) {
            throw new DomainException("El NIT es obligatorio");
        }
        if (restaurantModel.getDireccion() == null || restaurantModel.getDireccion().isBlank()) {
            throw new DomainException("La direccion es obligatoria");
        }
        if (restaurantModel.getTelefono() == null || restaurantModel.getTelefono().isBlank()) {
            throw new DomainException("El telefono es obligatorio");
        }
        if (restaurantModel.getUrlLogo() == null || restaurantModel.getUrlLogo().isBlank()) {
            throw new DomainException("La url del logo es obligatoria");
        }
        if (restaurantModel.getIdPropietario() == null) {
            throw new DomainException("El id del propietario es obligatorio");
        }

        UserModel propietario = userPersistencePort.findById(restaurantModel.getIdPropietario());
        if (propietario == null) {
            throw new DomainException("El usuario propietario no existe");
        }
        if (propietario.getRole() == null || !ROL_PROPIETARIO.equalsIgnoreCase(propietario.getRole().getNombre())) {
            throw new DomainException("El usuario indicado no tiene el rol propietario");
        }

        restaurantPersistencePort.saveRestaurant(restaurantModel);
    }

    @Override
    public List<RestaurantModel> getAllRestaurants() {
        return restaurantPersistencePort.getAllRestaurants();
    }

    @Override
    public PagedResult<RestaurantModel> listRestaurants(int page, int size) {
        if (page < 0) {
            throw new DomainException("El numero de pagina no puede ser negativo");
        }
        if (size <= 0) {
            throw new DomainException("El tamano de pagina debe ser mayor a 0");
        }
        return restaurantPersistencePort.listRestaurants(page, size);
    }
}
