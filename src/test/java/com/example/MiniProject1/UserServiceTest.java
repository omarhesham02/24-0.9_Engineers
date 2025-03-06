package com.example.MiniProject1;

import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;


    /*
public User addUser(User user)
public ArrayList<User> getUsers()
public User getUserById(UUID id)
public List<Order> getOrdersByUserId(UUID userId)
public void addOrderToUser(UUID userId, Order order)
public void emptyCart(UUID userId)
public void removeOrderFromUser(UUID userId, UUID orderId)
public void deleteUserById(UUID userId) throws Exception
*/

    @Test
    void addUser_withValidInput_shouldReturnSameUserData() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa");

        // Act
        User result = userService.addUser(user);

        // Assert
        assertEquals(user, result);
    }

    @Test
    void addUser_withDuplicateId_shouldThrowException() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa");

        // Act
        userService.addUser(user);

        // Assert
        assertThrows(Exception.class, () -> userService.addUser(user));
    }

    @Test
    void addUser_withNullName_shouldThrowException() {
        // Arrange
        User user = new User(UUID.randomUUID(), null);

        // Act & Assert
        assertThrows(Exception.class, () -> userService.addUser(user));
    }

    @Test
    void getUsers_withNoUsers_shouldReturnEmptyList() {
        // Arrange

        // Act
        ArrayList<User> result = userService.getUsers();

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getUsers_withMultipleUsers_shouldReturnAllUsers() {
        // Arrange
        User user1 = new User(UUID.randomUUID(), "Mo Tammaa");
        User user2 = new User(UUID.randomUUID(), "Omar Adel");
        userService.addUser(user1);
        userService.addUser(user2);

        // Act
        ArrayList<User> result = userService.getUsers();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    void getUsers_afterAddingInvalidUser_shouldNotReturnInvalidUser() {
        // Arrange
        User user1 = new User(UUID.randomUUID(), "Omar Tamer");
        User user2 = new User(UUID.randomUUID(), null);
        userService.addUser(user1);

        // Act
        userService.addUser(user2);
        ArrayList<User> result = userService.getUsers();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(user1));
        assertFalse(result.contains(user2));
    }

    @Test
    void getUserById_withValidId_shouldReturnCorrectUser() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Omar Hesham");
        User anotherUser = new User(UUID.randomUUID(), "Omar Adel");
        userService.addUser(user);
        userService.addUser(anotherUser);

        // Act
        User result = userService.getUserById(user.getId());

        // Assert
        assertEquals(user, result);
    }

    @Test
    void getUserById_withInvalidId_shouldReturnNull() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa");
        userService.addUser(user);

        // Act
        User result = userService.getUserById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    void getUserById_withNullId_shouldThrowException() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa");
        userService.addUser(user);

        // Act & Assert
        assertThrows(Exception.class, () -> userService.getUserById(null));
    }

    @Test
    void getOrdersByUserId_withValidUserIdAndOrder_shouldReturnUserOrders() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa"),
                anotherUser = new User(UUID.randomUUID(), "Omar Adel");

        ArrayList<Product> products = new ArrayList<>(List.of(
                new Product("Hohoz", 10),
                new Product("Shokalata Corona Dark bel bondoq", 50),
                new Product("V_Cola 3shan Pepsi moqat3a", 15))
        );
        ArrayList<Product> products1 = new ArrayList<>(List.of(new Product("Hohoz", 10)));
        ArrayList<Product> products2 = products.stream().filter(product -> !product.getName().equals("V_Cola 3shan Pepsi")).collect(Collectors.toCollection(ArrayList::new));

        Order order = new Order(user.getId(),
                products.stream().mapToDouble(Product::getPrice).sum(),
                products),
            order1 = new Order(user.getId(),
                products1.stream().mapToDouble(Product::getPrice).sum(),
                products1),
            order2 = new Order(user.getId(),
                        products2.stream().mapToDouble(Product::getPrice).sum(),
                        products2);

        userService.addUser(user);
        userService.addUser(anotherUser);
        userService.addOrderToUser(user.getId(), order);
        userService.addOrderToUser(user.getId(), order1);

        userService.addOrderToUser(user.getId(), order2);

        // Act
        List<Order> result = userService.getOrdersByUserId(user.getId());

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(order));
        assertTrue(result.contains(order1));
        assertFalse(result.contains(order2));
    }

    @Test
    void getOrdersByUserId_withInvalidUserId_shouldReturnEmptyList() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa");
        ArrayList<Product> products = new ArrayList<>(List.of(
                new Product("Hohoz", 10),
                new Product("Shokalata Corona Dark bel bondoq", 50),
                new Product("V_Cola 3shan Pepsi moqat3a", 15))
        );

        Order order1 = new Order(user.getId(),
                products.stream().mapToDouble(Product::getPrice).sum(),
                products);
        userService.addUser(user);
        userService.addOrderToUser(user.getId(), order1);

        // Act
        List<Order> result = userService.getOrdersByUserId(UUID.randomUUID());

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getOrdersByUserId_userWithNoOrders_shouldReturnEmptyList() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa");
        userService.addUser(user);

        // Act
        List<Order> result = userService.getOrdersByUserId(user.getId());

        // Assert
        assertEquals(0, result.size());
        assertNotNull(result);
    }







}