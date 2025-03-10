package com.example.MiniProject1;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.model.User;
import com.example.service.CartService;
import com.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CartServiceTest {
    @Autowired
    private CartService cartService;

    @Test
    void addCart_withValidInput_shouldReturnSameCartData() {
        User user = new User("Omar");
        Cart cart = new Cart(user.getId());

        Cart result = cartService.addCart(cart);

        assertEquals(cart, result);
    }

    @Test
    void addCart_withItemsInCart_shouldReturnCartWithSameItems() {
        User user = new User("Abdelaty");
        Cart cart = new Cart(user.getId());
        List<Product> products = new ArrayList<>();

        products.add(new Product("Product1", 10.99));
        products.add(new Product("Product2", 24.99));

        cart.setProducts(products);

        Cart result = cartService.addCart(cart);

        assertEquals(cart.getProducts().size(), result.getProducts().size());
        assertEquals(cart.getProducts(), result.getProducts());
    }

//    @Test
//    void addCart_withEmptyCart_shouldReturnEmptyCart() {
//        User user = new User("Bob");
//        Cart cart = new Cart(user.getId());
//        cart.setItems(Collections.emptyList());
//
//        Cart result = cartService.addCart(cart);
//
//        assertTrue(result.getItems().isEmpty());
//        assertEquals(cart, result);
//    }


//
//    @Test
//    void addUser_withNullName_shouldThrowException() {
//        // Arrange
//        User user = new User(UUID.randomUUID(), null);
//
//        // Act & Assert
//        assertThrows(Exception.class, () -> userService.addUser(user));
//    }

//    @Test
//    void getCarts() {
//    }
//
//    @Test
//    void getCartById() {
//    }
//
//    @Test
//    void getCartByUserId() {
//    }
//
//    @Test
//    void addProductToCart() {
//    }
//
//    @Test
//    void deleteProductFromCart() {
//    }
//
//    @Test
//    void deleteCartById() {
//    }
}