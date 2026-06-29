package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.DomainException;
import com.pragma.powerup.domain.exception.EntityNotFoundException;
import com.pragma.powerup.domain.model.DishModel;
import com.pragma.powerup.domain.model.OrderDishModel;
import com.pragma.powerup.domain.model.OrderModel;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.model.RestaurantModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IDishPersistencePort;
import com.pragma.powerup.domain.spi.IEmployeeRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.ISmsNotificationPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class OrderUseCaseTest {

    @Mock
    IOrderPersistencePort orderPersistencePort;
    @Mock
    IDishPersistencePort dishPersistencePort;
    @Mock
    IRestaurantPersistencePort restaurantPersistencePort;
    @Mock
    IUserPersistencePort userPersistencePort;
    @Mock
    IEmployeeRestaurantPersistencePort employeeRestaurantPersistencePort;
    @Mock
    ISmsNotificationPort smsNotificationPort;

    @InjectMocks
    OrderUseCase orderUseCase;

    private static final String CORREO_CLIENTE = "cliente@mail.com";
    private static final String CORREO_EMPLEADO = "empleado@mail.com";

    private UserModel cliente;
    private UserModel empleado;

    @BeforeEach
    void setUp() {
        cliente = new UserModel(1L, "Ana", "Cliente", 10L, "+573001112233", CORREO_CLIENTE, "x", null);
        empleado = new UserModel(2L, "Beto", "Empleado", 20L, "+573004445566", CORREO_EMPLEADO, "x", null);
    }

    private OrderModel newOrderRequest() {
        OrderModel order = new OrderModel();
        order.setIdRestaurante(5L);
        order.setPlatos(List.of(new OrderDishModel(null, 100L, 2)));
        return order;
    }

    private DishModel dish(Long id, Long restaurantId, boolean activo) {
        DishModel dish = new DishModel();
        dish.setId(id);
        dish.setNombre("Plato " + id);
        dish.setIdRestaurante(restaurantId);
        dish.setActivo(activo);
        return dish;
    }

    private OrderModel existingOrder(Long id, Long restaurantId, OrderStatus estado) {
        OrderModel order = new OrderModel();
        order.setId(id);
        order.setIdRestaurante(restaurantId);
        order.setIdCliente(cliente.getId());
        order.setEstado(estado);
        return order;
    }

    // ----- HU11: crear pedido ----------------------------------------------
    @Test
    void should_Create_Order_When_Data_Is_Valid() {
        OrderModel request = newOrderRequest();
        when(restaurantPersistencePort.findById(5L)).thenReturn(new RestaurantModel());
        when(userPersistencePort.findByCorreo(CORREO_CLIENTE)).thenReturn(cliente);
        when(orderPersistencePort.clientHasActiveOrder(cliente.getId())).thenReturn(false);
        when(dishPersistencePort.findById(100L)).thenReturn(dish(100L, 5L, true));
        when(orderPersistencePort.saveOrder(any(OrderModel.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderModel saved = orderUseCase.createOrder(request, CORREO_CLIENTE);

        assertEquals(OrderStatus.PENDIENTE, saved.getEstado());
        assertEquals(cliente.getId(), saved.getIdCliente());
        assertNull(saved.getPin());
        assertNull(saved.getIdChef());
        verify(orderPersistencePort).saveOrder(request);
    }

    @Test
    void should_Throw_When_Restaurant_Is_Null() {
        OrderModel request = newOrderRequest();
        request.setIdRestaurante(null);
        assertThrows(DomainException.class, () -> orderUseCase.createOrder(request, CORREO_CLIENTE));
    }

    @Test
    void should_Throw_When_Order_Has_No_Dishes() {
        OrderModel request = newOrderRequest();
        request.setPlatos(List.of());
        assertThrows(DomainException.class, () -> orderUseCase.createOrder(request, CORREO_CLIENTE));
    }

    @Test
    void should_Throw_When_Restaurant_Does_Not_Exist() {
        OrderModel request = newOrderRequest();
        when(restaurantPersistencePort.findById(5L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderUseCase.createOrder(request, CORREO_CLIENTE));
    }

    @Test
    void should_Throw_When_Client_Has_Active_Order() {
        OrderModel request = newOrderRequest();
        when(restaurantPersistencePort.findById(5L)).thenReturn(new RestaurantModel());
        when(userPersistencePort.findByCorreo(CORREO_CLIENTE)).thenReturn(cliente);
        when(orderPersistencePort.clientHasActiveOrder(cliente.getId())).thenReturn(true);
        assertThrows(DomainException.class, () -> orderUseCase.createOrder(request, CORREO_CLIENTE));
        verify(orderPersistencePort, never()).saveOrder(any());
    }

    @Test
    void should_Throw_When_Dish_Belongs_To_Other_Restaurant() {
        OrderModel request = newOrderRequest();
        when(restaurantPersistencePort.findById(5L)).thenReturn(new RestaurantModel());
        when(userPersistencePort.findByCorreo(CORREO_CLIENTE)).thenReturn(cliente);
        when(orderPersistencePort.clientHasActiveOrder(cliente.getId())).thenReturn(false);
        when(dishPersistencePort.findById(100L)).thenReturn(dish(100L, 999L, true));
        assertThrows(DomainException.class, () -> orderUseCase.createOrder(request, CORREO_CLIENTE));
    }

    @Test
    void should_Throw_When_Dish_Is_Inactive() {
        OrderModel request = newOrderRequest();
        when(restaurantPersistencePort.findById(5L)).thenReturn(new RestaurantModel());
        when(userPersistencePort.findByCorreo(CORREO_CLIENTE)).thenReturn(cliente);
        when(orderPersistencePort.clientHasActiveOrder(cliente.getId())).thenReturn(false);
        when(dishPersistencePort.findById(100L)).thenReturn(dish(100L, 5L, false));
        assertThrows(DomainException.class, () -> orderUseCase.createOrder(request, CORREO_CLIENTE));
    }

    @Test
    void should_Throw_When_Dish_Quantity_Is_Not_Positive() {
        OrderModel request = newOrderRequest();
        request.setPlatos(List.of(new OrderDishModel(null, 100L, 0)));
        when(restaurantPersistencePort.findById(5L)).thenReturn(new RestaurantModel());
        when(userPersistencePort.findByCorreo(CORREO_CLIENTE)).thenReturn(cliente);
        when(orderPersistencePort.clientHasActiveOrder(cliente.getId())).thenReturn(false);
        assertThrows(DomainException.class, () -> orderUseCase.createOrder(request, CORREO_CLIENTE));
    }

    // ----- HU12: listar pedidos --------------------------------------------
    @Test
    void should_List_Orders_For_Employee_Restaurant() {
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(5L);

        orderUseCase.listOrders(OrderStatus.PENDIENTE, 0, 10, CORREO_EMPLEADO);

        verify(orderPersistencePort).listByRestaurantAndStatus(eq(5L), eq(OrderStatus.PENDIENTE), eq(0), eq(10));
    }

    @Test
    void should_Throw_When_Page_Is_Negative() {
        assertThrows(DomainException.class, () -> orderUseCase.listOrders(null, -1, 10, CORREO_EMPLEADO));
    }

    @Test
    void should_Throw_When_Size_Is_Not_Positive() {
        assertThrows(DomainException.class, () -> orderUseCase.listOrders(null, 0, 0, CORREO_EMPLEADO));
    }

    @Test
    void should_Throw_When_Employee_Has_No_Restaurant() {
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(null);
        assertThrows(DomainException.class, () -> orderUseCase.listOrders(null, 0, 10, CORREO_EMPLEADO));
    }

    // ----- HU13: asignar y pasar a en preparación --------------------------
    @Test
    void should_Assign_And_Start_Preparation() {
        OrderModel order = existingOrder(50L, 5L, OrderStatus.PENDIENTE);
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(5L);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        when(orderPersistencePort.updateOrder(order)).thenReturn(order);

        OrderModel result = orderUseCase.assignAndStartPreparation(50L, CORREO_EMPLEADO);

        assertEquals(OrderStatus.EN_PREPARACION, result.getEstado());
        assertEquals(empleado.getId(), result.getIdChef());
    }

    @Test
    void should_Throw_When_Assigning_Order_Not_Pending() {
        OrderModel order = existingOrder(50L, 5L, OrderStatus.EN_PREPARACION);
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(5L);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        assertThrows(DomainException.class, () -> orderUseCase.assignAndStartPreparation(50L, CORREO_EMPLEADO));
    }

    @Test
    void should_Throw_When_Order_Belongs_To_Other_Restaurant() {
        OrderModel order = existingOrder(50L, 999L, OrderStatus.PENDIENTE);
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(5L);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        assertThrows(DomainException.class, () -> orderUseCase.assignAndStartPreparation(50L, CORREO_EMPLEADO));
    }

    @Test
    void should_Throw_When_Order_Not_Found() {
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(5L);
        when(orderPersistencePort.findById(50L)).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> orderUseCase.assignAndStartPreparation(50L, CORREO_EMPLEADO));
    }

    // ----- HU14: marcar listo + SMS ----------------------------------------
    @Test
    void should_Mark_Ready_Generate_Pin_And_Notify() {
        OrderModel order = existingOrder(50L, 5L, OrderStatus.EN_PREPARACION);
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(5L);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        when(orderPersistencePort.updateOrder(order)).thenAnswer(inv -> inv.getArgument(0));
        when(userPersistencePort.findById(cliente.getId())).thenReturn(cliente);

        OrderModel result = orderUseCase.markReady(50L, CORREO_EMPLEADO);

        assertEquals(OrderStatus.LISTO, result.getEstado());
        verify(smsNotificationPort).sendOrderReadySms(eq(cliente.getCelular()), eq(result.getPin()));
    }

    @Test
    void should_Throw_When_Marking_Ready_Order_Not_In_Preparation() {
        OrderModel order = existingOrder(50L, 5L, OrderStatus.PENDIENTE);
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(5L);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        assertThrows(DomainException.class, () -> orderUseCase.markReady(50L, CORREO_EMPLEADO));
        verify(smsNotificationPort, never()).sendOrderReadySms(any(), any());
    }

    // ----- HU15: entregar pedido -------------------------------------------
    @Test
    void should_Mark_Delivered_When_Pin_Matches() {
        OrderModel order = existingOrder(50L, 5L, OrderStatus.LISTO);
        order.setPin("1234");
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(5L);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        when(orderPersistencePort.updateOrder(order)).thenAnswer(inv -> inv.getArgument(0));

        OrderModel result = orderUseCase.markDelivered(50L, "1234", CORREO_EMPLEADO);

        assertEquals(OrderStatus.ENTREGADO, result.getEstado());
    }

    @Test
    void should_Throw_When_Pin_Is_Blank() {
        assertThrows(DomainException.class, () -> orderUseCase.markDelivered(50L, " ", CORREO_EMPLEADO));
    }

    @Test
    void should_Throw_When_Pin_Does_Not_Match() {
        OrderModel order = existingOrder(50L, 5L, OrderStatus.LISTO);
        order.setPin("1234");
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(5L);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        assertThrows(DomainException.class, () -> orderUseCase.markDelivered(50L, "0000", CORREO_EMPLEADO));
    }

    @Test
    void should_Throw_When_Delivering_Order_Not_Ready() {
        OrderModel order = existingOrder(50L, 5L, OrderStatus.EN_PREPARACION);
        order.setPin("1234");
        when(userPersistencePort.findByCorreo(CORREO_EMPLEADO)).thenReturn(empleado);
        when(employeeRestaurantPersistencePort.findRestaurantIdByEmployeeId(empleado.getId())).thenReturn(5L);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        assertThrows(DomainException.class, () -> orderUseCase.markDelivered(50L, "1234", CORREO_EMPLEADO));
    }

    // ----- HU16: cancelar pedido -------------------------------------------
    @Test
    void should_Cancel_Order_When_Pending_And_Owned_By_Client() {
        OrderModel order = existingOrder(50L, 5L, OrderStatus.PENDIENTE);
        when(userPersistencePort.findByCorreo(CORREO_CLIENTE)).thenReturn(cliente);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        when(orderPersistencePort.updateOrder(order)).thenAnswer(inv -> inv.getArgument(0));

        OrderModel result = orderUseCase.cancelOrder(50L, CORREO_CLIENTE);

        assertEquals(OrderStatus.CANCELADO, result.getEstado());
    }

    @Test
    void should_Throw_When_Cancelling_Other_Clients_Order() {
        OrderModel order = existingOrder(50L, 5L, OrderStatus.PENDIENTE);
        order.setIdCliente(999L);
        when(userPersistencePort.findByCorreo(CORREO_CLIENTE)).thenReturn(cliente);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        assertThrows(DomainException.class, () -> orderUseCase.cancelOrder(50L, CORREO_CLIENTE));
    }

    @Test
    void should_Throw_When_Cancelling_Order_Already_In_Preparation() {
        OrderModel order = existingOrder(50L, 5L, OrderStatus.EN_PREPARACION);
        when(userPersistencePort.findByCorreo(CORREO_CLIENTE)).thenReturn(cliente);
        when(orderPersistencePort.findById(50L)).thenReturn(order);
        assertThrows(DomainException.class, () -> orderUseCase.cancelOrder(50L, CORREO_CLIENTE));
    }
}
