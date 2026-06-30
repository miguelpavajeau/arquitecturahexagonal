package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.DishRequestDto;
import com.pragma.powerup.application.dto.request.DishUpdateRequestDto;
import com.pragma.powerup.application.dto.response.DishListResponseDto;
import com.pragma.powerup.application.dto.response.PageResponseDto;
import com.pragma.powerup.application.handler.IDishHandler;
import com.pragma.powerup.application.mapper.IDishRequestMapper;
import com.pragma.powerup.application.mapper.IDishResponseMapper;
import com.pragma.powerup.domain.api.IDishServicePort;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DishHandler implements IDishHandler {

    private final IDishServicePort dishServicePort;
    private final IDishRequestMapper dishRequestMapper;
    private final IDishResponseMapper dishResponseMapper;

    @Override
    public void saveDish(DishRequestDto dishRequestDto) {
        DishModel dishModel = dishRequestMapper.toDish(dishRequestDto);
        dishServicePort.saveDish(dishModel, getAuthenticatedCorreo());
    }

    @Override
    public void updateDish(Long dishId, DishUpdateRequestDto dishUpdateRequestDto) {
        dishServicePort.updateDish(dishId, dishUpdateRequestDto.getPrecio(), dishUpdateRequestDto.getDescripcion(),
                getAuthenticatedCorreo());
    }

    @Override
    public void changeDishStatus(Long dishId, Boolean activo) {
        dishServicePort.changeDishStatus(dishId, activo, getAuthenticatedCorreo());
    }

    @Override
    public PageResponseDto<DishListResponseDto> listDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size) {
        PagedResult<DishModel> result = dishServicePort.listDishesByRestaurant(restaurantId, categoryId, page, size);
        return new PageResponseDto<>(
                dishResponseMapper.toListResponseList(result.getContent()),
                result.getPageNumber(),
                result.getPageSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    private String getAuthenticatedCorreo() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
