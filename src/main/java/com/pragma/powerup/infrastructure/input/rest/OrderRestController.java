package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.DeliverOrderRequestDto;
import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.dto.response.PageResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderRestController {

    private final IOrderHandler orderHandler;

    @Operation(summary = "Place a new order (client)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid data or client already has an active order", content = @Content),
            @ApiResponse(responseCode = "404", description = "Restaurant or dish not found", content = @Content)
    })
    @PreAuthorize("hasRole('CLIENTE')")
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        return new ResponseEntity<>(orderHandler.createOrder(orderRequestDto), HttpStatus.CREATED);
    }

    @Operation(summary = "List the orders of the employee's restaurant, filtered by status (paginated)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paginated orders returned", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid status or pagination parameters", content = @Content)
    })
    @PreAuthorize("hasRole('EMPLEADO')")
    @GetMapping
    public ResponseEntity<PageResponseDto<OrderResponseDto>> listOrders(
            @RequestParam(required = false) String estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(orderHandler.listOrders(estado, page, size));
    }

    @Operation(summary = "Assign an order to the employee and set it to in-preparation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order assigned and in preparation", content = @Content),
            @ApiResponse(responseCode = "400", description = "Order not in a valid state or not from your restaurant", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @PreAuthorize("hasRole('EMPLEADO')")
    @PatchMapping("/{orderId}/assign")
    public ResponseEntity<OrderResponseDto> assignOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderHandler.assignOrder(orderId));
    }

    @Operation(summary = "Mark an order as ready and notify the client via SMS with a PIN")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order ready, client notified", content = @Content),
            @ApiResponse(responseCode = "400", description = "Order not in a valid state or not from your restaurant", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @PreAuthorize("hasRole('EMPLEADO')")
    @PatchMapping("/{orderId}/ready")
    public ResponseEntity<OrderResponseDto> markReady(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderHandler.markReady(orderId));
    }

    @Operation(summary = "Deliver an order (requires the client's PIN)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order delivered", content = @Content),
            @ApiResponse(responseCode = "400", description = "Wrong PIN, order not ready, or not from your restaurant", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @PreAuthorize("hasRole('EMPLEADO')")
    @PatchMapping("/{orderId}/delivered")
    public ResponseEntity<OrderResponseDto> deliverOrder(@PathVariable Long orderId,
                                                         @Valid @RequestBody DeliverOrderRequestDto deliverOrderRequestDto) {
        return ResponseEntity.ok(orderHandler.deliverOrder(orderId, deliverOrderRequestDto.getPin()));
    }

    @Operation(summary = "Cancel an order (client, only while pending)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order cancelled", content = @Content),
            @ApiResponse(responseCode = "400", description = "Order can no longer be cancelled", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content)
    })
    @PreAuthorize("hasRole('CLIENTE')")
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderHandler.cancelOrder(orderId));
    }
}
