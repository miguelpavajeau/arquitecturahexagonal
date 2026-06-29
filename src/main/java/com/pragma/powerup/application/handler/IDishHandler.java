package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishUpdateRequestDto;
import com.pragma.powerup.application.dto.response.DishListResponseDto;
import com.pragma.powerup.application.dto.response.PageResponseDto;

public interface IDishHandler {

    void saveDish(DishRequestDto dishRequestDto);

    void updateDish(Long dishId, DishUpdateRequestDto dishUpdateRequestDto);

    void changeDishStatus(Long dishId, Boolean activo);

    PageResponseDto<DishListResponseDto> listDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size);
}
