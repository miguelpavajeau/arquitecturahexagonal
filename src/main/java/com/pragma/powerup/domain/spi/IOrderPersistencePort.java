package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.model.PagedResult;

public interface IOrderPersistencePort {

    OrderModel saveOrder(OrderModel orderModel);

    OrderModel updateOrder(OrderModel orderModel);

    OrderModel findById(Long id);

    boolean clientHasActiveOrder(Long idCliente);

    PagedResult<OrderModel> listByRestaurantAndStatus(Long idRestaurante, OrderStatus estado, int page, int size);
}
