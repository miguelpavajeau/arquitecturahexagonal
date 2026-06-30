package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.infrastructure.out.jpa.entity.DishEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDishRepository extends JpaRepository<DishEntity, Long> {

    Page<DishEntity> findByIdRestauranteAndActivoTrue(Long idRestaurante, Pageable pageable);

    Page<DishEntity> findByIdRestauranteAndCategoriaIdAndActivoTrue(Long idRestaurante, Long categoriaId, Pageable pageable);
}
