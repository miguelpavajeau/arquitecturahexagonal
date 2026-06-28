package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.exception.EntityNotFoundException;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.OrderDishModel;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.model.PagedResult;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.ISmsNotificationPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;

import java.security.SecureRandom;
import java.time.LocalDateTime;

public class OrderUseCase implements IOrderServicePort {

    private static final String MENSAJE_CANCELACION_INVALIDA =
            "Lo sentimos, tu pedido ya está en preparación y no puede cancelarse";

    private final IOrderPersistencePort orderPersistencePort;
    private final IDishPersistencePort dishPersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;
    private final IUserPersistencePort userPersistencePort;
    private final IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    private final ISmsNotificationPort smsNotificationPort;
    private final SecureRandom random = new SecureRandom();

    public OrderUseCase(IOrderPersistencePort orderPersistencePort,
                        IDishPersistencePort dishPersistencePort,
                        IRestaurantPersistencePort restaurantPersistencePort,
                        IUserPersistencePort userPersistencePort,
                        IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort,
                        ISmsNotificationPort smsNotificationPort) {
        this.orderPersistencePort = orderPersistencePort;
        this.dishPersistencePort = dishPersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
        this.userPersistencePort = userPersistencePort;
        this.employeeRestaurantPersistencePort = employeeRestaurantPersistencePort;
        this.smsNotificationPort = smsNotificationPort;
    }

    // ----- HU11: realizar pedido -------------------------------------------
    @Override
    public OrderModel createOrder(OrderModel orderModel, String correoCliente) {
        if (orderModel.getIdRestaurante() == null) {
            throw new DomainException("El restaurante es obligatorio");
        }
        if (orderModel.getPlatos() == null || orderModel.getPlatos().isEmpty()) {
            throw new DomainException("El pedido debe contener al menos un plato");
        }
        if (restaurantPersistencePort.findById(orderModel.getIdRestaurante()) == null) {
            throw new EntityNotFoundException("El restaurante no existe");
        }

        UserModel cliente = userPersistencePort.findByCorreo(correoCliente);
        if (cliente == null) {
            throw new EntityNotFoundException("El cliente no existe");
        }
        if (orderPersistencePort.clientHasActiveOrder(cliente.getId())) {
            throw new DomainException("Ya tienes un pedido en proceso, no puedes crear uno nuevo");
        }

        for (OrderDishModel item : orderModel.getPlatos()) {
            if (item.getIdPlato() == null) {
                throw new DomainException("Cada plato del pedido es obligatorio");
            }
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new DomainException("La cantidad de cada plato debe ser mayor a 0");
            }
            DishModel dish = dishPersistencePort.findById(item.getIdPlato());
            if (dish == null) {
                throw new EntityNotFoundException("El plato " + item.getIdPlato() + " no existe");
            }
            if (!dish.getIdRestaurante().equals(orderModel.getIdRestaurante())) {
                throw new DomainException("Todos los platos deben pertenecer al mismo restaurante");
            }
            if (Boolean.FALSE.equals(dish.getActivo())) {
                throw new DomainException("El plato " + dish.getNombre() + " no está disponible");
            }
        }

        orderModel.setIdCliente(cliente.getId());
        orderModel.setFecha(LocalDateTime.now());
        orderModel.setEstado(OrderStatus.PENDIENTE);
        orderModel.setIdChef(null);
        orderModel.setPin(null);
        return orderPersistencePort.saveOrder(orderModel);
    }

    // ----- HU12: listar pedidos por estado ---------------------------------
    @Override
    public PagedResult<OrderModel> listOrders(OrderStatus estado, int page, int size, String correoEmpleado) {
        if (page < 0) {
            throw new DomainException("El numero de pagina no puede ser negativo");
        }
        if (size <= 0) {
            throw new DomainException("El tamano de pagina debe ser mayor a 0");
        }
        Long restaurantId = resolveEmployeeRestaurant(correoEmpleado);
        return orderPersistencePort.listByRestaurantAndStatus(restaurantId, estado, page, size);
    }

    // ----- HU13: asignarse y pasar a "en preparación" ----------------------
    @Override
    public OrderModel assignAndStartPreparation(Long orderId, String correoEmpleado) {
        UserModel empleado = userPersistencePort.findByCorreo(correoEmpleado);
        Long restaurantId = resolveEmployeeRestaurant(correoEmpleado);
        OrderModel order = getOrderOfRestaurant(orderId, restaurantId);

        if (order.getEstado() != OrderStatus.PENDIENTE) {
            throw new DomainException("Solo los pedidos en estado Pendiente pueden pasar a En preparación");
        }
        order.setIdChef(empleado.getId());
        order.setEstado(OrderStatus.EN_PREPARACION);
        return orderPersistencePort.updateOrder(order);
    }

    // ----- HU14: marcar listo + notificar por SMS --------------------------
    @Override
    public OrderModel markReady(Long orderId, String correoEmpleado) {
        Long restaurantId = resolveEmployeeRestaurant(correoEmpleado);
        OrderModel order = getOrderOfRestaurant(orderId, restaurantId);

        if (order.getEstado() != OrderStatus.EN_PREPARACION) {
            throw new DomainException("Solo los pedidos en preparación pueden marcarse como Listo");
        }
        order.setEstado(OrderStatus.LISTO);
        order.setPin(generatePin());
        OrderModel saved = orderPersistencePort.updateOrder(order);

        UserModel cliente = userPersistencePort.findById(saved.getIdCliente());
        if (cliente != null && cliente.getCelular() != null) {
            smsNotificationPort.sendOrderReadySms(cliente.getCelular(), saved.getPin());
        }
        return saved;
    }

    // ----- HU15: entregar pedido (valida pin) ------------------------------
    @Override
    public OrderModel markDelivered(Long orderId, String pin, String correoEmpleado) {
        if (pin == null || pin.isBlank()) {
            throw new DomainException("El pin de seguridad es obligatorio");
        }
        Long restaurantId = resolveEmployeeRestaurant(correoEmpleado);
        OrderModel order = getOrderOfRestaurant(orderId, restaurantId);

        if (order.getEstado() != OrderStatus.LISTO) {
            throw new DomainException("Solo los pedidos en estado Listo pueden entregarse");
        }
        if (!pin.equals(order.getPin())) {
            throw new DomainException("El pin de seguridad no es correcto");
        }
        order.setEstado(OrderStatus.ENTREGADO);
        return orderPersistencePort.updateOrder(order);
    }

    // ----- HU16: cancelar pedido -------------------------------------------
    @Override
    public OrderModel cancelOrder(Long orderId, String correoCliente) {
        UserModel cliente = userPersistencePort.findByCorreo(correoCliente);
        if (cliente == null) {
            throw new EntityNotFoundException("El cliente no existe");
        }
        OrderModel order = orderPersistencePort.findById(orderId);
        if (order == null) {
            throw new EntityNotFoundException("El pedido no existe");
        }
        if (!order.getIdCliente().equals(cliente.getId())) {
            throw new DomainException("Solo puedes cancelar tus propios pedidos");
        }
        if (order.getEstado() != OrderStatus.PENDIENTE) {
            throw new DomainException(MENSAJE_CANCELACION_INVALIDA);
        }
        order.setEstado(OrderStatus.CANCELADO);
        return orderPersistencePort.updateOrder(order);
    }

    // ----- helpers ---------------------------------------------------------
    private Long resolveEmployeeRestaurant(String correoEmpleado) {
        UserModel empleado = userPersistencePort.findByCorreo(correoEmpleado);
        if (empleado == null) {
            throw new EntityNotFoundException("El empleado no existe");
        }
        Long restaurantId = employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId());
        if (restaurantId == null) {
            throw new DomainException("El empleado no está asociado a ningún restaurante");
        }
        return restaurantId;
    }

    private OrderModel getOrderOfRestaurant(Long orderId, Long restaurantId) {
        OrderModel order = orderPersistencePort.findById(orderId);
        if (order == null) {
            throw new EntityNotFoundException("El pedido no existe");
        }
        if (!order.getIdRestaurante().equals(restaurantId)) {
            throw new DomainException("El pedido no pertenece a tu restaurante");
        }
        return order;
    }

    private String generatePin() {
        return String.format("%04d", random.nextInt(10000));
    }
}
