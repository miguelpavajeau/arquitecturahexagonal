package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.PagedResult;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.DishEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IDishEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IDishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DishJpaAdapter implements IDishPersistencePort {

    private final IDishRepository dishRepository;
    private final IDishEntityMapper dishEntityMapper;

    @Override
    public DishModel saveDish(DishModel dishModel) {
        DishEntity dishEntity = dishRepository.save(dishEntityMapper.toEntity(dishModel));
        return dishEntityMapper.toDishModel(dishEntity);
    }

    @Override
    public DishModel findById(Long id) {
        return dishRepository.findById(id)
                .map(dishEntityMapper::toDishModel)
                .orElse(null);
    }

    @Override
    public DishModel updateDish(DishModel dishModel) {
        DishEntity dishEntity = dishRepository.save(dishEntityMapper.toEntity(dishModel));
        return dishEntityMapper.toDishModel(dishEntity);
    }

    @Override
    public PagedResult<DishModel> listActiveDishesByRestaurant(Long restaurantId, Long categoryId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("nombre").ascending());
        Page<DishEntity> entityPage = (categoryId == null)
                ? dishRepository.findByIdRestauranteAndActivoTrue(restaurantId, pageRequest)
                : dishRepository.findByIdRestauranteAndCategoriaIdAndActivoTrue(restaurantId, categoryId, pageRequest);

        return new PagedResult<>(
                entityPage.getContent().stream()
                        .map(dishEntityMapper::toDishModel)
                        .collect(Collectors.toList()),
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages());
    }
}
