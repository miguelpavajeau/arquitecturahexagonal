package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.PagedResult;
import com.pragma.powerup.domain.model.RestaurantModel;

import java.util.List;

public interface IRestaurantPersistencePort {

    RestaurantModel saveRestaurant(RestaurantModel restaurantModel);

    RestaurantModel findById(Long id);

    /** Primer restaurante registrado por el propietario indicado, o null si no tiene. */
    RestaurantModel findByPropietarioId(Long idPropietario);

    List<RestaurantModel> getAllRestaurants();

    PagedResult<RestaurantModel> listRestaurants(int page, int size);
}
