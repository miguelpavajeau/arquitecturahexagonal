package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.model.PagedResult;

public interface IOrderServicePort {

    OrderModel createOrder(OrderModel orderModel, String correoCliente);

    PagedResult<OrderModel> listOrders(OrderStatus estado, int page, int size, String correoEmpleado);

    OrderModel assignAndStartPreparation(Long orderId, String correoEmpleado);

    OrderModel markReady(Long orderId, String correoEmpleado);

    OrderModel markDelivered(Long orderId, String pin, String correoEmpleado);

    OrderModel cancelOrder(Long orderId, String correoCliente);
}
