package com.example.MiniProject1;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;


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
        User user = new User(UUID.randomUUID(), "Mo Tammaa2");

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
        User user1 = new User(UUID.randomUUID(), "Mo Tammaa3");
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
        int currentUserCount = userService.getUsers().size();

        User user1 = new User(UUID.randomUUID(), "Omar Tamer");
        User user2 = new User(UUID.randomUUID(), null);
        userService.addUser(user1);

        // Act
        try {
            userService.addUser(user2);
            fail("Expected HttpClientErrorException to be thrown");
        } catch (HttpClientErrorException e) {
            // Exception is expected, continue with assertions
        }

        // Assert
        ArrayList<User> result = userService.getUsers();
        assertEquals(currentUserCount + 1, result.size());
        assertTrue(result.contains(user1));
        assertFalse(result.contains(user2));
    }

    @Test
    void getUserById_withValidId_shouldReturnCorrectUser() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Omar Hesham");
        User anotherUser = new User(UUID.randomUUID(), "Omar Adel2");
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
        User user = new User(UUID.randomUUID(), "Mo Tammaa4");
        userService.addUser(user);

        // Act
        User result = userService.getUserById(UUID.randomUUID());

        // Assert
        assertNull(result);
    }

    @Test
    void getUserById_withNullId_shouldThrowException() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa5");
        userService.addUser(user);

        // Act & Assert
        assertThrows(Exception.class, () -> userService.getUserById(null));
    }

    @Test
    void getOrdersByUserId_withValidUserIdAndOrder_shouldReturnUserOrders() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa6"),
                anotherUser = new User(UUID.randomUUID(), "Omar Adel3");

        ArrayList<Product> products = new ArrayList<>(List.of(
                new Product("Hohoz", 10),
                new Product("Shokalata Corona Dark bel bondoq", 50),
                new Product("V_Cola 3shan Pepsi moqat3a", 15))
        );
        ArrayList<Product> products1 = new ArrayList<>(List.of(new Product("Hohoz", 10)));

        Cart cart = cartService.getCartByUserId(user.getId()),
                cart1 = cartService.getCartByUserId(anotherUser.getId());


        for (Product product : products)    cartService.addProductToCart(cart.getId(), product);
        for (Product product : products1) cartService.addProductToCart(cart1.getId(), product);


        // Act
        List<Order> result = userService.getOrdersByUserId(user.getId());

        // Assert
        assertEquals(1, result.size());
        assertEquals(products.stream().mapToDouble(Product::getPrice).sum(), result.getFirst().getTotalPrice());
        assertEquals(products, result.getFirst().getProducts());
    }

    @Test
    void getOrdersByUserId_withInvalidUserId_shouldReturnEmptyList() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa7");
        ArrayList<Product> products = new ArrayList<>(List.of(
                new Product("Hohoz", 10),
                new Product("Shokalata Corona Dark bel bondoq", 50),
                new Product("V_Cola 3shan Pepsi moqat3a", 15))
        );

        Cart cart = cartService.getCartByUserId(user.getId());
        for (Product product : products) cartService.addProductToCart(cart.getId(), product);

        // Act
        List<Order> result = userService.getOrdersByUserId(UUID.randomUUID());

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    void getOrdersByUserId_userWithNoOrders_shouldReturnEmptyList() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Mo Tammaa8");
        userService.addUser(user);

        // Act
        List<Order> result = userService.getOrdersByUserId(user.getId());

        // Assert
        assertEquals(0, result.size());
        assertNotNull(result);
    }

    @Test
    void addOrderToUser_shouldAddOrder_whenUserExists() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Test User 8");
        userService.addUser(user);

        Cart cart = new Cart(user.getId());
        cartService.addCart(cart);

        Product product = new Product("Test Product", 100.0);

        cartService.addProductToCart(cart.getId(), product);

        // Act
        userService.addOrderToUser(user.getId());

        // Assert
        List<Order> orders = userService.getOrdersByUserId(user.getId());
        assertFalse(orders.isEmpty());
        assertEquals(100.0, orders.getFirst().getTotalPrice());
    }

    @Test
    void addOrderToUser_shouldNotAddOrder_whenUserDoesNotExist() {
        // Arrange
        UUID nonExistentUserId = UUID.randomUUID();

        // Act & Assert
        assertThrows(Exception.class, () -> userService.addOrderToUser(nonExistentUserId));
    }

    @Test
    void addOrderToUser_shouldEmptyCart_afterOrderCreation() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Test User 9");
        userService.addUser(user);

        Cart cart = cartService.getCartByUserId(user.getId());
        Product product = new Product("Test Product", 100.0);
        cartService.addProductToCart(cart.getId(), product);

        // Act
        userService.addOrderToUser(user.getId());

        // Assert
        cart = cartService.getCartByUserId(user.getId());
        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    void emptyCart_shouldRemoveAllProducts_whenUserExistsAndCartExists() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Test User 10");
        userService.addUser(user);

        Product product1 = new Product("Test Product 1", 100.0);
        Product product2 = new Product("Test Product 2", 200.0);
        Product product3 = new Product("Test Product 3", 300.0);

        Cart cart = new Cart(user.getId());

        cartService.addProductToCart(user.getId(), product1);
        cartService.addProductToCart(user.getId(), product2);
        cartService.addProductToCart(user.getId(), product3);


        cartService.addCart(cart);

        // Act
        userService.emptyCart(user.getId());

        // Assert
        cart = cartService.getCartByUserId(user.getId());
        assertTrue(cart.getProducts().isEmpty());
    }

    @Test
    void emptyCart_shouldThrowException_whenUserDoesNotExist() {
        // Arrange
        UUID nonExistentUserId = UUID.randomUUID();

        // Act & Assert
        assertThrows(Exception.class, () -> userService.emptyCart(nonExistentUserId));
    }

    @Test
    void emptyUserCart_ShouldThrowException_WhenCartDoesNotExist() {
        // Arrange
        UUID nonExistentUserId = UUID.randomUUID();

        // Act & Assert
        assertThrows(Exception.class, () -> userService.emptyCart(nonExistentUserId));
    }

    @Test
    void removeOrderFromUser_shouldRemoveOrder_whenUserExistsAndOrderExists() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Test User 11");
        userService.addUser(user);

        Cart cart = cartService.getCartByUserId(user.getId());
        Product product = new Product("Test Product", 100.0);
        cartService.addProductToCart(cart.getId(), product);

        userService.addOrderToUser(user.getId());
        List<Order> orders = userService.getOrdersByUserId(user.getId());
        Order order = orders.getFirst();

        // Act
        userService.removeOrderFromUser(user.getId(), order.getId());

        // Assert
        List<Order> updatedOrders = userService.getOrdersByUserId(user.getId());
        assertTrue(updatedOrders.isEmpty());
    }

    @Test
    void removeOrderFromUser_shouldThrowException_whenUserDoesNotExist() {
        // Arrange
        UUID nonExistentUserId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();

        // Act & Assert
        assertThrows(Exception.class, () ->
            userService.removeOrderFromUser(nonExistentUserId, orderId));
    }

    @Test
    void removeOrderFromUser_shouldThrowException_whenOrderDoesNotExist() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Test User 12");
        userService.addUser(user);
        UUID nonExistentOrderId = UUID.randomUUID();

        // Act & Assert
        assertThrows(Exception.class, () ->
            userService.removeOrderFromUser(user.getId(), nonExistentOrderId));
    }

    @Test
    void deleteUserById_shouldDeleteUser_whenUserExists() {
        // Arrange
        User user = new User(UUID.randomUUID(), "Test User 13");
        userService.addUser(user);

        // Act
        userService.deleteUserById(user.getId());

        // Assert
        assertNull(userService.getUserById(user.getId()));
    }

    @Test
    void deleteUserById_shouldThrowException_whenUserDoesNotExist() {
        // Arrange
        UUID nonExistentUserId = UUID.randomUUID();

        // Act & Assert
        assertThrows(Exception.class, () -> userService.deleteUserById(nonExistentUserId));
    }

    @Test
    void deleteUserById_shouldThrowException_whenUserIdIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(null));
    }

}