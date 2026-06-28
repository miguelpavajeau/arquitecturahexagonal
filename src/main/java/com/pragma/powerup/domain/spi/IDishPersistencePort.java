package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.PagedResult;

public interface IDishPersistencePort {

    DishModel saveDish(DishModel dishModel);

    DishModel findById(Long id);

    DishModel updateDish(DishModel dishModel);

    PagedResult<DishModel> listActiveDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size);
}
