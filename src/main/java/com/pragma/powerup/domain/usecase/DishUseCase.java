package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.exception.EntityNotFoundException;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.PagedResult;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

public class DishUseCase implements IDishServicePort {

    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserPersistencePort userPersistencePort;

    public DishUseCase(IDishPersistencePort dishPersistencePort, IRestaurantPersistencePort restaurantPersistencePort,
                        IUserPersistencePort userPersistencePort) {
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userPersistencePort = userPersistencePort;
    }

    @Override
    public void saveDish(DishModel dishModel, String correoAutenticado) {
        if (dishModel.getNombre() == null || dishModel.getNombre().isBlank()) {
            throw new DomainException("El nombre del plato es obligatorio");
        }
        if (dishModel.getPrecio() == null || dishModel.getPrecio() <= 0) {
            throw new DomainException("El precio debe ser un numero entero positivo mayor a 0");
        }
        if (dishModel.getDescripcion() == null || dishModel.getDescripcion().isBlank()) {
            throw new DomainException("La descripcion del plato es obligatoria");
        }
        if (dishModel.getUrlImagen() == null || dishModel.getUrlImagen().isBlank()) {
            throw new DomainException("La url de la imagen es obligatoria");
        }
        if (dishModel.getIdRestaurante() == null) {
            throw new DomainException("El restaurante es obligatorio");
        }
        if (dishModel.getCategoria() == null || dishModel.getCategoria().getId() == null) {
            throw new DomainException("La categoria es obligatoria");
        }

        validateOwnership(dishModel.getIdRestaurante(), correoAutenticado);

        dishModel.setActivo(true);
        dishPersistencePort.saveDish(dishModel);
    }

    @Override
    public void updateDish(Long dishId, Integer precio, String descripcion, String correoAutenticado) {
        DishModel existingDish = dishPersistencePort.findById(dishId);
        if (existingDish == null) {
            throw new EntityNotFoundException("El plato no existe");
        }

        validateOwnership(existingDish.getIdRestaurante(), correoAutenticado);

        if (precio == null || precio <= 0) {
            throw new DomainException("El precio debe ser un numero entero positivo mayor a 0");
        }
        if (descripcion == null || descripcion.isBlank()) {
            throw new DomainException("La descripcion del plato es obligatoria");
        }

        existingDish.setPrecio(precio);
        existingDish.setDescripcion(descripcion);
        dishPersistencePort.updateDish(existingDish);
    }

    @Override
    public void changeDishStatus(Long dishId, Boolean activo, String correoAutenticado) {
        if (activo == null) {
            throw new DomainException("El estado del plato es obligatorio");
        }
        DishModel existingDish = dishPersistencePort.findById(dishId);
        if (existingDish == null) {
            throw new EntityNotFoundException("El plato no existe");
        }

        validateOwnership(existingDish.getIdRestaurante(), correoAutenticado);

        existingDish.setActivo(activo);
        dishPersistencePort.updateDish(existingDish);
    }

    @Override
    public PagedResult<DishModel> listDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size) {
        if (restaurantId == null) {
            throw new DomainException("El restaurante es obligatorio");
        }
        if (page < 0) {
            throw new DomainException("El numero de pagina no puede ser negativo");
        }
        if (size <= 0) {
            throw new DomainException("El tamano de pagina debe ser mayor a 0");
        }
        if (restaurantPersistencePort.findById(restaurantId) == null) {
            throw new EntityNotFoundException("El restaurante no existe");
        }
        return dishPersistencePort.listActiveDishesByRestaurant(restaurantId, categoryId, page, size);
    }

    private void validateOwnership(Long idRestaurante, String correoAutenticado) {
        RestaurantModel restaurantModel = restaurantPersistencePort.findById(idRestaurante);
        if (restaurantModel == null) {
            throw new EntityNotFoundException("El restaurante no existe");
        }

        UserModel userModel = userPersistencePort.findByCorreo(correoAutenticado);
        if (userModel == null || !restaurantModel.getIdPropietario().equals(userModel.getId())) {
            throw new DomainException("Solo el propietario del restaurante puede gestionar sus platos");
        }
    }
}
