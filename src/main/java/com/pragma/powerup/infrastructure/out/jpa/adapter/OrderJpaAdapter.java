package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.OrderDishModel;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.model.PagedResult;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderDishEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class OrderJpaAdapter implements IOrderPersistencePort {

    private static final List<OrderStatus> ACTIVE_STATES =
            Arrays.asList(OrderStatus.PENDIENTE, OrderStatus.EN_PREPARACION, OrderStatus.LISTO);

    private final IOrderRepository orderRepository;

    @Override
    public OrderModel saveOrder(OrderModel orderModel) {
        return toModel(orderRepository.save(toEntity(orderModel)));
    }

    @Override
    public OrderModel updateOrder(OrderModel orderModel) {
        return toModel(orderRepository.save(toEntity(orderModel)));
    }

    @Override
    public OrderModel findById(Long id) {
        return orderRepository.findById(id).map(this::toModel).orElse(null);
    }

    @Override
    public boolean clientHasActiveOrder(Long idCliente) {
        return orderRepository.existsByIdClienteAndEstadoIn(idCliente, ACTIVE_STATES);
    }

    @Override
    public PagedResult<OrderModel> listByRestaurantAndStatus(Long idRestaurante, OrderStatus estado, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("fecha").ascending());
        Page<OrderEntity> entityPage = (estado == null)
                ? orderRepository.findByIdRestaurante(idRestaurante, pageRequest)
                : orderRepository.findByIdRestauranteAndEstado(idRestaurante, estado, pageRequest);

        return new PagedResult<>(
                entityPage.getContent().stream().map(this::toModel).collect(Collectors.toList()),
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages());
    }

    private OrderEntity toEntity(OrderModel model) {
        OrderEntity entity = new OrderEntity();
        entity.setId(model.getId());
        entity.setIdCliente(model.getIdCliente());
        entity.setIdRestaurante(model.getIdRestaurante());
        entity.setFecha(model.getFecha());
        entity.setEstado(model.getEstado());
        entity.setIdChef(model.getIdChef());
        entity.setPin(model.getPin());
        if (model.getPlatos() != null) {
            entity.setPlatos(model.getPlatos().stream().map(item -> {
                OrderDishEntity dishEntity = new OrderDishEntity();
                dishEntity.setId(item.getId());
                dishEntity.setIdPlato(item.getIdPlato());
                dishEntity.setCantidad(item.getCantidad());
                return dishEntity;
            }).collect(Collectors.toList()));
        }
        return entity;
    }

    private OrderModel toModel(OrderEntity entity) {
        List<OrderDishModel> platos = entity.getPlatos() == null ? null
                : entity.getPlatos().stream()
                .map(item -> new OrderDishModel(item.getId(), item.getIdPlato(), item.getCantidad()))
                .collect(Collectors.toList());
        return new OrderModel(
                entity.getId(),
                entity.getIdCliente(),
                entity.getIdRestaurante(),
                entity.getFecha(),
                entity.getEstado(),
                entity.getIdChef(),
                entity.getPin(),
                platos);
    }
}
