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
import org.springframework.web.client.HttpClientErrorException;

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
    void addUser_ShouldReturnUser_WhenUserIsAdded() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User";
        User user = new User(testUserId, testUserName);
        when(userRepository.addUser(user)).thenReturn(user);

        // Act
        User result = userService.addUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(testUserName, result.getName());
        assertEquals(testUserId, result.getId());
        verify(userRepository, times(1)).addUser(user);
    }

    @Test
    void addUser_ShouldReturnUser_WhenUserIsAddedWithDifferentName() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User 2";
        User user = new User(testUserId, testUserName);
        when(userRepository.addUser(user)).thenReturn(user);

        // Act
        User result = userService.addUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(testUserName, result.getName());
        assertEquals(testUserId, result.getId());
    }

    @Test
    void addUser_ShouldReturnUserWithOrders_WhenUserHasOrders() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User 3";
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(UUID.randomUUID(), 100.0, new ArrayList<>()));
        orders.add(new Order(UUID.randomUUID(), 200.0, new ArrayList<>()));
        orders.add(new Order(UUID.randomUUID(), 300.0, new ArrayList<>()));
        User user = new User(testUserId, testUserName, orders);
        when(userRepository.addUser(user)).thenReturn(user);

        // Act
        User result = userService.addUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(testUserName, result.getName());
        assertEquals(testUserId, result.getId());
        assertEquals(3, result.getOrders().size());
        verify(userRepository, times(1)).addUser(user);
    }

    @Test
    void getUsers_ShouldReturnListOfUsers_WhenUsersExist() {
        // Arrange
        List<User> users = new ArrayList<>();
        users.add(new User(UUID.randomUUID(), "Test User 1"));
        users.add(new User(UUID.randomUUID(), "Test User 2"));
        users.add(new User(UUID.randomUUID(), "Test User 3"));
        when(userRepository.getAllUsers()).thenReturn(users);

        // Act
        List<User> result = userService.getUsers();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(userRepository, times(1)).getAllUsers();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User 4";
        User user = new User(testUserId, testUserName);
        when(userRepository.getUserById(testUserId)).thenReturn(user);

        // Act
        User result = userService.getUserById(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(testUserName, result.getName());
        assertEquals(testUserId, result.getId());
        verify(userRepository, times(1)).getUserById(testUserId);
    }

    @Test
    void getUserById_ShouldReturnNull_WhenUserDoesNotExist() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        when(userRepository.getUserById(testUserId)).thenReturn(null);

        // Act
        User result = userService.getUserById(testUserId);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).getUserById(testUserId);
    }

    @Test
    void getUserById_ShouldReturnUserWithOrders_WhenUserHasOrders() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        String testUserName = "Test User 5";
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(UUID.randomUUID(), 100.0, new ArrayList<>()));
        orders.add(new Order(UUID.randomUUID(), 200.0, new ArrayList<>()));
        orders.add(new Order(UUID.randomUUID(), 300.0, new ArrayList<>()));
        User user = new User(testUserId, testUserName, orders);
        when(userRepository.getUserById(testUserId)).thenReturn(user);

        // Act
        User result = userService.getUserById(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(testUserName, result.getName());
        assertEquals(testUserId, result.getId());
        assertEquals(3, result.getOrders().size());
        verify(userRepository, times(1)).getUserById(testUserId);
    }

    @Test
    void getOrdersByUserId_ShouldReturnOrders_WhenUserHasOrders() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        ArrayList<Order> orders = new ArrayList<>();
        orders.add(new Order(UUID.randomUUID(), 100.0, new ArrayList<>()));
        orders.add(new Order(UUID.randomUUID(), 200.0, new ArrayList<>()));
        orders.add(new Order(UUID.randomUUID(), 300.0, new ArrayList<>()));
        when(userRepository.getOrdersByUserId(testUserId)).thenReturn(orders);

        // Act
        List<Order> result = userService.getOrdersByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        verify(userRepository, times(1)).getOrdersByUserId(testUserId);
    }

    @Test
    void getOrdersByUserId_ShouldReturnEmptyList_WhenUserHasNoOrders() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        ArrayList<Order> orders = new ArrayList<>();
        when(userRepository.getOrdersByUserId(testUserId)).thenReturn(orders);

        // Act
        List<Order> result = userService.getOrdersByUserId(testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.size());
        verify(userRepository, times(1)).getOrdersByUserId(testUserId);
    }

    @Test
    void getOrdersByUserId_ShouldReturnNull_WhenUserDoesNotExist() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        when(userRepository.getUserById(testUserId)).thenReturn(null);

        // Act
        List<Order> result = userService.getOrdersByUserId(testUserId);

        // Assert
        assertNull(result);
        verify(userRepository, times(1)).getUserById(testUserId);
        verify(userRepository, times(0)).getOrdersByUserId(testUserId);
    }

    @Test
    void addOrderToUser_ShouldAddOrder_WhenUserExists() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        User user = new User(testUserId, "Test User 8");
        Order order = new Order(UUID.randomUUID(), 100.0, new ArrayList<>());
        when(userRepository.getUserById(testUserId)).thenReturn(user);

        // Act
        userService.addOrderToUser(testUserId);

        // Assert
        assertTrue(user.getOrders().contains(order));
        verify(userRepository, times(1)).addOrderToUser(testUserId, order);
    }

    @Test
    void addOrderToUser_ShouldNotAddOrder_WhenUserDoesNotExist() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        Order order = new Order(UUID.randomUUID(), 100.0, new ArrayList<>());
        when(userRepository.getUserById(testUserId)).thenReturn(null);

        // Act
        userService.addOrderToUser(testUserId);

        // Assert
        verify(userRepository, times(0)).addOrderToUser(testUserId, order);
    }

    @Test
    void addOrderToUser_ShouldNotAddOrder_WhenOrderIsNull() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        User user = new User(testUserId, "Test User 8");
        when(userRepository.getUserById(testUserId)).thenReturn(user);

        // Act
        userService.addOrderToUser(testUserId);

        // Assert
        assertTrue(user.getOrders().isEmpty());
        verify(userRepository, times(0)).addOrderToUser(testUserId, null);
    }

    @Test
    void emptyUserCart_ShouldRemoveCart_WhenUserExistsAndCartExists() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        User user = new User(testUserId, "Test User 8");
        when(userRepository.getUserById(testUserId)).thenReturn(user);

        // Act
        userService.emptyCart(testUserId);

        // Assert
        assertTrue(user.getOrders().isEmpty());
        verify(userRepository, times(1)).emptyCart(testUserId);
    }

    @Test
    void emptyUserCart_ShouldNotRemoveCart_WhenUserDoesNotExist() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        when(userRepository.getUserById(testUserId)).thenReturn(null);

        // Act
        userService.emptyCart(testUserId);

        // Assert
        verify(userRepository, times(0)).emptyCart(testUserId);
    }

    @Test
    void emptyUserCart_ShouldNotRemoveCart_WhenCartDoesNotExist() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        User user = new User(testUserId, "Test User 8");
        when(userRepository.getUserById(testUserId)).thenReturn(user);

        // Act
        userService.emptyCart(testUserId);

        // Assert
        assertTrue(user.getOrders().isEmpty());
        verify(userRepository, times(0)).emptyCart(testUserId);
    }

    @Test
    void removeOrderFromUser_ShouldRemoveOrder_WhenUserExistsAndOrderExists() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        UUID testOrderId = UUID.randomUUID();
        User user = new User(testUserId, "Test User 8");
        Order order = new Order(testOrderId, testUserId, 100.0, new ArrayList<>());

        user.addOrder(order);
        when(userRepository.getUserById(testUserId)).thenReturn(user);

        // Act
        userService.removeOrderFromUser(testUserId, testOrderId);

        // Assert
        assertTrue(user.getOrders().isEmpty());
        verify(userRepository, times(1)).removeOrderFromUser(testUserId, testOrderId);
    }

    @Test
    void removeOrderFromUser_ShouldNotRemoveOrder_WhenUserDoesNotExist() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        UUID testOrderId = UUID.randomUUID();
        when(userRepository.getUserById(testUserId)).thenReturn(null);

        // Act
        userService.removeOrderFromUser(testUserId, testOrderId);

        // Assert
        verify(userRepository, times(0)).removeOrderFromUser(testUserId, testOrderId);
    }

    @Test
    void removeOrderFromUser_ShouldNotRemoveOrder_WhenOrderDoesNotExist() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        UUID testOrderId = UUID.randomUUID();
        User user = new User(testUserId, "Test User 8");
        when(userRepository.getUserById(testUserId)).thenReturn(user);

        // Act
        userService.removeOrderFromUser(testUserId, testOrderId);

        // Assert
        assertTrue(user.getOrders().isEmpty());
        verify(userRepository, times(0)).removeOrderFromUser(testUserId, testOrderId);
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Arrange
        UUID testUserId = UUID.randomUUID();
        User user = new User(testUserId, "Test User 8");
        when(userRepository.getUserById(testUserId)).thenReturn(user);

        // Act
        userService.deleteUserById(testUserId);

        // Assert
        verify(userRepository, times(1)).deleteUser(testUserId);
    }

@Test
void deleteUser_ShouldNotDeleteUser_WhenUserDoesNotExist() {
    // Arrange
    UUID testUserId = UUID.randomUUID();
    when(userRepository.getUserById(testUserId)).thenReturn(null);

    // Act & Assert
    assertThrows(HttpClientErrorException.class, () -> userService.deleteUserById(testUserId));
}

@Test
void deleteUser_ShouldNotDeleteUser_WhenUserIsNull() {
    // Arrange
    UUID userId = null;
    when(userRepository.getUserById(userId)).thenReturn(null);

    // Act & Assert
    assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(null));
    }

}