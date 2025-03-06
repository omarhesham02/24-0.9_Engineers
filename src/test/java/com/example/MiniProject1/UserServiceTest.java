package com.example.MiniProject1;

import com.example.model.Order;
import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser1() {
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User";

        User user = new User(testUserId, testUserName);
        when(userRepository.addUser(user)).thenReturn(user);

        User result = userService.addUser(user);

        assertNotNull(result);
        assertEquals(testUserName, result.getName());
        assertEquals(testUserId, result.getId());
        verify(userRepository, times(1)).addUser(user);
    }

    @Test
    void testAddUser2() {
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User 2";

        User user = new User(testUserId, testUserName);
        when(userRepository.addUser(user)).thenReturn(user);

        User result = userService.addUser(user);

        assertNotNull(result);
        assertEquals(testUserName, result.getName());
        assertEquals(testUserId, result.getId());

    }

    @Test
    void testAddUserWithOrders() {
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User 3";
        ArrayList<Order> orders = new ArrayList<>();

        Order order1 = new Order(UUID.randomUUID(), 100.0, new ArrayList<>());
        Order order2 = new Order(UUID.randomUUID(), 200.0, new ArrayList<>());
        Order order3 = new Order(UUID.randomUUID(), 300.0, new ArrayList<>());

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        User user = new User(testUserId, testUserName, orders);
        when(userRepository.addUser(user)).thenReturn(user);

        User result = userService.addUser(user);

        assertNotNull(result);
        assertEquals(testUserName, result.getName());
        assertEquals(testUserId, result.getId());
        assertEquals(3, result.getOrders().size());
        verify(userRepository, times(1)).addUser(user);
    }


    @Test
    void testGetUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), "Test User 1"));
        users.add(new User(UUID.randomUUID(), "Test User 2"));
        users.add(new User(UUID.randomUUID(), "Test User 3"));

        when(userRepository.getAllUsers()).thenReturn(users);

        List<User> result = userService.getUsers();

        assertNotNull(result);
        assertEquals(3, result.size());
        verify(userRepository, times(1)).getAllUsers();
    }

    @Test
    void testGetUserById() {
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User 4";
        User user = new User(testUserId, testUserName);
        when(userRepository.getUserById(testUserId)).thenReturn(user);
        User result = userService.getUserById(testUserId);
        assertNotNull(result);
        assertEquals(testUserName, result.getName());
        assertEquals(testUserId, result.getId());
        verify(userRepository, times(1)).getUserById(testUserId);
    }

    @Test
    void testGetUserbyIdNotFound() {
        UUID testUserId = UUID.randomUUID();
        when(userRepository.getUserById(testUserId)).thenReturn(null);
        User result = userService.getUserById(testUserId);
        assertNull(result);
        verify(userRepository, times(1)).getUserById(testUserId);
    }

    @Test
    void testGetUserByIdHasOrders() {
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User 5";
        ArrayList<Order> orders = new ArrayList<>();

        Order order1 = new Order(UUID.randomUUID(), 100.0, new ArrayList<>());
        Order order2 = new Order(UUID.randomUUID(), 200.0, new ArrayList<>());
        Order order3 = new Order(UUID.randomUUID(), 300.0, new ArrayList<>());

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        User user = new User(testUserId, testUserName, orders);
        when(userRepository.getUserById(testUserId)).thenReturn(user);
        User result = userService.getUserById(testUserId);
        assertNotNull(result);
        assertEquals(testUserName, result.getName());
        assertEquals(testUserId, result.getId());
        assertEquals(3, result.getOrders().size());
        verify(userRepository, times(1)).getUserById(testUserId);
    }

    @Test
    void testGetOrdersByUserIdUserExistsHasOrders() {
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User 6";
        ArrayList<Order> orders = new ArrayList<>();

        Order order1 = new Order(UUID.randomUUID(), 100.0, new ArrayList<>());
        Order order2 = new Order(UUID.randomUUID(), 200.0, new ArrayList<>());
        Order order3 = new Order(UUID.randomUUID(), 300.0, new ArrayList<>());

        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        when(userRepository.getOrdersByUserId(testUserId)).thenReturn(orders);

        List<Order> result = userService.getOrdersByUserId(testUserId);
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(userRepository, times(1)).getOrdersByUserId(testUserId);
    }

    @Test
    void testGetOrdersByUserIdUserExistsHasNoOrders() {
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User 7";
        ArrayList<Order> orders = new ArrayList<>();
        when(userRepository.getOrdersByUserId(testUserId)).thenReturn(orders);
        List<Order> result = userService.getOrdersByUserId(testUserId);
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository, times(1)).getOrdersByUserId(testUserId);
    }

    @Test
    void testGetOrdersByUserIdUserDoesNotExist() {
        UUID testUserId = UUID.randomUUID();
        when(userRepository.getOrdersByUserId(testUserId)).thenReturn(null);
        List<Order> result = userService.getOrdersByUserId(testUserId);
        assertNull(result);
        verify(userRepository, times(1)).getOrdersByUserId(testUserId);
    }


    @Test
    void testAddOrderToUser() {
        UUID testUserId = UUID.randomUUID();
        User user = new User(testUserId, "Test User 8");
        Order order = new Order(UUID.randomUUID(), 100.0, new ArrayList<>());
        when(userRepository.getUserById(testUserId)).thenReturn(user);
        userService.addOrderToUser(testUserId);

        assertTrue(user.getOrders().contains(order));

        verify(userRepository, times(1)).addOrderToUser(testUserId, order);
    }

    @Test
    void testAddOrderToUseUserDoesNotExist() {
        UUID testUserId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), 100.0, new ArrayList<>());
        when(userRepository.getUserById(testUserId)).thenReturn(null);
        userService.addOrderToUser(testUserId);
        verify(userRepository, times(1)).addOrderToUser(testUserId, order);
    }

    @Test
    void testAddOrderToUserOrderDoesNotExist() {
        UUID testUserId = UUID.randomUUID();
        User user = new User(testUserId, "Test User 8");
        when(userRepository.getUserById(testUserId)).thenReturn(user);
        userService.addOrderToUser(testUserId);

        assertTrue(user.getOrders().isEmpty());
        verify(userRepository, times(1)).addOrderToUser(testUserId, null);
    }
}