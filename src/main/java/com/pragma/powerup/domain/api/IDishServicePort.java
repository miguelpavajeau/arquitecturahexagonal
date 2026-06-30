package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.PagedResult;

public interface IDishServicePort {

    void saveDish(DishModel dishModel, String correoAutenticado);

    void updateDish(Long dishId, Integer precio, String descripcion, String correoAutenticado);

    void changeDishStatus(Long dishId, Boolean activo, String correoAutenticado);

    PagedResult<DishModel> listDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size);
}
