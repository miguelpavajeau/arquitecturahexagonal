package com.pragma.powerup.infrastructure.out.jpa.repository;

import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IOrderRepository extends JpaRepository<OrderEntity, Long> {

    boolean existsByIdClienteAndEstadoIn(Long idCliente, List<OrderStatus> estados);

    Page<OrderEntity> findByIdRestaurante(Long idRestaurante, Pageable pageable);

    Page<OrderEntity> findByIdRestauranteAndEstado(Long idRestaurante, OrderStatus estado, Pageable pageable);
}
