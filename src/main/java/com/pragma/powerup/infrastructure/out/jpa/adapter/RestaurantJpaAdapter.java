package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.PagedResult;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public RestaurantModel saveRestaurant(RestaurantModel restaurantModel) {
        RestaurantEntity restaurantEntity = restaurantRepository.save(restaurantEntityMapper.toEntity(restaurantModel));
        return restaurantEntityMapper.toRestaurantModel(restaurantEntity);
    }

    @Override
    public RestaurantModel findById(Long id) {
        return restaurantRepository.findById(id)
                .map(restaurantEntityMapper::toRestaurantModel)
                .orElse(null);
    }

    @Override
    public RestaurantModel findByPropietarioId(Long idPropietario) {
        RestaurantEntity entity = restaurantRepository.findFirstByIdPropietario(idPropietario);
        return entity == null ? null : restaurantEntityMapper.toRestaurantModel(entity);
    }

    @Override
    public List<RestaurantModel> getAllRestaurants() {
        return restaurantEntityMapper.toRestaurantModelList(
                restaurantRepository.findAll(Sort.by("nombre").ascending()));
    }

    @Override
    public PagedResult<RestaurantModel> listRestaurants(int page, int size) {
        Page<RestaurantEntity> entityPage = restaurantRepository.findAll(
                PageRequest.of(page, size, Sort.by("nombre").ascending()));
        return new PagedResult<>(
                restaurantEntityMapper.toRestaurantModelList(entityPage.getContent()),
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages());
    }
}
