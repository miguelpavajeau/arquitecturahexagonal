package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.dto.response.PageResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.application.mapper.IOrderResponseMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.model.PagedResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderServicePort orderServicePort;
    private final IOrderRequestMapper orderRequestMapper;
    private final IOrderResponseMapper orderResponseMapper;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        OrderModel order = orderServicePort.createOrder(
                orderRequestMapper.toModel(orderRequestDto), getAuthenticatedCorreo());
        return orderResponseMapper.toResponse(order);
    }

    @Override
    public PageResponseDto<OrderResponseDto> listOrders(String estado, int page, int size) {
        PagedResult<OrderModel> result =
                orderServicePort.listOrders(parseStatus(estado), page, size, getAuthenticatedCorreo());
        return new PageResponseDto<>(
                orderResponseMapper.toResponseList(result.getContent()),
                result.getPageNumber(),
                result.getPageSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    @Override
    public OrderResponseDto assignOrder(Long orderId) {
        return orderResponseMapper.toResponse(
                orderServicePort.assignAndStartPreparation(orderId, getAuthenticatedCorreo()));
    }

    @Override
    public OrderResponseDto markReady(Long orderId) {
        return orderResponseMapper.toResponse(
                orderServicePort.markReady(orderId, getAuthenticatedCorreo()));
    }

    @Override
    public OrderResponseDto deliverOrder(Long orderId, String pin) {
        return orderResponseMapper.toResponse(
                orderServicePort.markDelivered(orderId, pin, getAuthenticatedCorreo()));
    }

    @Override
    public OrderResponseDto cancelOrder(Long orderId) {
        return orderResponseMapper.toResponse(
                orderServicePort.cancelOrder(orderId, getAuthenticatedCorreo()));
    }

    private OrderStatus parseStatus(String estado) {
        if (estado == null || estado.isBlank()) {
            return null;
        }
        try {
            return OrderStatus.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new DomainException("El estado '" + estado + "' no es válido");
        }
    }

    private String getAuthenticatedCorreo() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
