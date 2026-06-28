package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.PagedResult;
import com.pragma.powerup.domain.model.RestaurantModel;

import java.util.List;

public interface IRestaurantServicePort {

    void saveRestaurant(RestaurantModel restaurantModel);

    List<RestaurantModel> getAllRestaurants();

    PagedResult<RestaurantModel> listRestaurants(int page, int size);
}
