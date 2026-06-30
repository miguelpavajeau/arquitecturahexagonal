package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.dto.response.PageResponseDto;

public interface IOrderHandler {

    OrderResponseDto createOrder(OrderRequestDto orderRequestDto);

    PageResponseDto<OrderResponseDto> listOrders(String estado, int page, int size);

    OrderResponseDto assignOrder(Long orderId);

    OrderResponseDto markReady(Long orderId);

    OrderResponseDto deliverOrder(Long orderId, String pin);

    OrderResponseDto cancelOrder(Long orderId);
}
