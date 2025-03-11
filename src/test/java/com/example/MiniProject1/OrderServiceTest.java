package com.example.MiniProject1;

import com.example.model.User;
import com.example.repository.OrderRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.service.OrderService;
import com.example.model.Order;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderRepository orderRepository;

    private ArrayList<Order> ordersJSON;

    @BeforeAll
    void backupData() {
        ordersJSON = new ArrayList<>(orderService.getOrders());
        orderRepository.clearAll();
    }

    @AfterAll
    void restoreData() {
        orderRepository.saveAll(ordersJSON);
    }

    @Test
    void addOrder_validOrder_shouldAddOrder() {
        // Arrange
        User user = new User("Mohamed Tammaa");
        Order order = new Order(UUID.randomUUID(), user.getId(), 100.0, new ArrayList<>());

        // Act
        orderService.addOrder(order);

        // Assert
        Order retrievedOrder = orderService.getOrderById(order.getId());
        assertNotNull(retrievedOrder);
        assertEquals(order.getId(), retrievedOrder.getId());
        assertEquals(order.getUserId(), retrievedOrder.getUserId());
    }

    @Test
    void addOrder_nullOrder_shouldThrowException() {
        // Arrange
        Order order = null;

        // Act & Assert
        //noinspection ConstantValue
        assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order));
    }

    @Test
    void addOrder_nullUser_shouldThrowException() {
        // Arrange
        Order order = new Order(UUID.randomUUID(), null, 100.0, new ArrayList<>());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.addOrder(order), "Order should have a user");
    }

    @Test
    void getOrders_addMultipleOrders_shouldReturnAllOrders() {
        // Arrange
        User user = new User("Omar Adel"), user2 = new User("Hussein");
        Order order1 = new Order(UUID.randomUUID(), user.getId(), 100.0, new ArrayList<>()),
                order2 = new Order(UUID.randomUUID(), user2.getId(), 200.0, new ArrayList<>());

        orderService.addOrder(order1);
        orderService.addOrder(order2);

        // Act
        ArrayList<Order> orders = orderService.getOrders();

        // Assert
        assertFalse(orders.isEmpty());
        assertTrue(orders.contains(order1));
        assertTrue(orders.contains(order2));
        assertEquals(2, orders.stream().filter(o ->
            o.getId().equals(order1.getId()) || o.getId().equals(order2.getId())
        ).count());
        assertEquals(2, orders.stream().filter(o ->
            o.getUserId().equals(user.getId()) || o.getUserId().equals(user2.getId())
        ).count());
    }

    @Test
    void getOrders_noOrders_shouldReturnEmptyList() {

        // Arrange
        orderRepository.clearAll();

        // Act
        ArrayList<Order> orders = orderService.getOrders();

        // Assert
        assertTrue(orders.isEmpty());
    }

    @Test
    void getOrders_afterAddingInvalidOrder_shouldReturnEmptyList() {
        // Arrange
        orderRepository.clearAll();
        Order order = new Order(UUID.randomUUID(), null, 100.0, new ArrayList<>());
        try { orderService.addOrder(order); } catch (IllegalArgumentException ignored) {}

        // Act
        ArrayList<Order> orders = orderService.getOrders();

        // Assert
        assertTrue(orders.isEmpty());
    }


    @Test
    void getOrderById_validOrderId_shouldReturnOrder() {
        // Arrange
        User user = new User("Mohamed Tammaa");
        Order order = new Order(UUID.randomUUID(), user.getId(), 100.0, new ArrayList<>());
        orderService.addOrder(order);

        // Act
        Order retrievedOrder = orderService.getOrderById(order.getId());

        // Assert
        assertNotNull(retrievedOrder);
        assertEquals(order.getId(), retrievedOrder.getId());
        assertEquals(order.getUserId(), retrievedOrder.getUserId());
    }

    @Test
    void getOrderById_nonExistingOrderId_shouldReturnNull() {
        // Arrange
        UUID orderId = UUID.randomUUID();

        // Act
        Order retrievedOrder = orderService.getOrderById(orderId);

        // Assert
        assertNull(retrievedOrder);
    }

    @Test
    void getOrderById_nullOrderId_shouldThrowException() {
        // Arrange
        UUID orderId = null;

        // Act & Assert
        //noinspection ConstantValue
        assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void deleteOrderById_validOrderId_shouldDeleteOrder() {
        // Arrange
        User user = new User("Mohamed Tammaa");
        Order order = new Order(UUID.randomUUID(), user.getId(), 100.0, new ArrayList<>());
        orderService.addOrder(order);

        // Act
        orderService.deleteOrderById(order.getId());

        // Assert
        assertNull(orderService.getOrderById(order.getId()));
    }

    @Test
    void deleteOrderById_nonExistingOrderId_shouldThrowException() {
        // Arrange
        UUID orderId = UUID.randomUUID();

        // Act & Assert
        assertThrows(HttpStatusCodeException.class, () -> orderService.deleteOrderById(orderId));
    }

    @Test
    void deleteOrderById_nullOrderId_shouldThrowException() {
        // Arrange
        UUID orderId = null;

        // Act & Assert
        //noinspection ConstantValue
        assertThrows(IllegalArgumentException.class, () -> orderService.deleteOrderById(orderId));
    }

}