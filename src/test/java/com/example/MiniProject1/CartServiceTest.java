package com.example.MiniProject1;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.service.CartService;
import com.example.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CartServiceTest {
    @Autowired
    private CartService cartService;

    private ArrayList<Cart> cartsJSON;
    @Autowired
    private CartRepository cartRepository;

    @BeforeEach
    void backupData() {
        cartsJSON = new ArrayList<>(cartService.getCarts());
        cartRepository.saveAll(new ArrayList<>());
    }

    @AfterEach
    void restoreData() {
        cartRepository.saveAll(cartsJSON);
    }

    @Test
    void addCart_withValidInput_shouldReturnSameCartData() {
        User user = new User("Omar");
        Cart cart = new Cart(user.getId());

        Cart result = cartService.addCart(cart);

        assertEquals(cart, result);
    }

    @Test
    void addCart_withValidInput_shouldSaveCartInJSON() {
        User user = new User("Omar");
        Cart cart = new Cart(user.getId());

        Cart result = cartService.addCart(cart);

        boolean cartExists = cartService.getCarts().stream()
                .anyMatch(jsonCart -> jsonCart.getId().equals(result.getId()));

        assertTrue(cartExists, "Cart should be saved in the JSON");
    }

    @Test
    void addCart_withItemsInCart_shouldSaveCartWithSameNumberOfProducts() {
        User user = new User("Abdelaty");
        Cart cart = new Cart(user.getId());
        List<Product> products = new ArrayList<>();

        products.add(new Product("Product1", 10.99));
        products.add(new Product("Product2", 24.99));

        cart.setProducts(products);

        Cart result = cartService.addCart(cart);

        Optional<Cart> savedCart = cartService.getCarts().stream()
                .filter(jsonCart -> jsonCart.getId().equals(result.getId()))
                .findFirst();

        assertTrue(savedCart.isPresent(), "Cart should be saved in the JSON");

        assertEquals(products.size(), savedCart.get().getProducts().size(),
                "Saved cart should have the same number of products as original cart");
    }

    @Test
    void getCarts_shouldReturnEmptyList_whenNoCarts() {
        ArrayList<Cart> carts = cartService.getCarts();

        assertTrue(carts.isEmpty(), "Cart should be empty");
    }

    @Test
    void getCarts_shouldReturnSameCarts() {
        User user = new User("Tamer");
        Cart cart = new Cart(user.getId());

        cartService.addCart(cart);

        List<Cart> carts = cartService.getCarts();

        assertEquals(1, carts.size(), "Cart should be same as original cart");
        assertEquals(cart, carts.get(0));
    }

    @Test
    void getCarts_shouldReturnEmpty_whenCartDeleted() {
        User user = new User("Tamer");
        Cart cart = new Cart(user.getId());

        cartService.addCart(cart);
        cartService.deleteCartById(cart.getId());

        List<Cart> carts = cartService.getCarts();

        assertTrue(carts.isEmpty(), "Cart should be empty");
    }

    @Test
    void getCartById_shouldReturnNull_whenCartNotFound() {
        assertNull(cartService.getCartById(UUID.randomUUID()));
    }

    @Test
    void getCartById_shouldReturnCart_whenCartFound() {
        User user = new User("Tamer");
        Cart cart = new Cart(user.getId());

        cartService.addCart(cart);

        Cart result = cartService.getCartById(cart.getId());

        assertNotNull(result, "Cart should not be null");
        assertEquals(cart, result);
        assertEquals(cart.getId(), result.getId());
    }

    @Test
    void getCartById_afterAddingProductsToCart_shouldReturnCartWithProducts() {
        User user = new User("OT");
        Cart cart = new Cart(user.getId());

        List<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", 999.99));
        products.add(new Product("Mouse", 29.99));
        cart.setProducts(products);

        Cart savedCart = cartService.addCart(cart);

        Cart result = cartService.getCartById(savedCart.getId());

        assertNotNull(result, "Cart should not be null");
        assertEquals(2, result.getProducts().size(), "Cart should have 2 products");

        boolean hasLaptop = result.getProducts().stream()
                .anyMatch(product -> product.getName().equals("Laptop"));
        boolean hasMouse = result.getProducts().stream()
                .anyMatch(product -> product.getName().equals("Mouse"));

        assertTrue(hasLaptop, "Cart should contain the laptop product");
        assertTrue(hasMouse, "Cart should contain the mouse product");
    }

    @Test
    void getCartByUserId_shouldReturnNull_whenCartNotFound() {
        assertNull(cartService.getCartByUserId(UUID.randomUUID()));
    }

    @Test
    void getCartByUserId_shouldReturnCart_whenCartFound() {
        User user = new User("Tamer");
        Cart cart = new Cart(user.getId());

        cartService.addCart(cart);

        Cart result = cartService.getCartByUserId(user.getId());

        assertNotNull(result, "Cart should not be null");
        assertEquals(cart, result);
        assertEquals(cart.getId(), result.getId());
    }

    @Test
    void getCartByUserId_afterAddingProductsToCart_shouldReturnCartWithProducts() {
        User user = new User("OT");
        Cart cart = new Cart(user.getId());

        List<Product> products = new ArrayList<>();
        products.add(new Product("Laptop", 999.99));
        products.add(new Product("Mouse", 29.99));
        cart.setProducts(products);

        Cart savedCart = cartService.addCart(cart);

        Cart result = cartService.getCartByUserId(user.getId());

        assertNotNull(result, "Cart should not be null");
        assertEquals(2, result.getProducts().size(), "Cart should have 2 products");

        boolean hasLaptop = result.getProducts().stream()
                .anyMatch(product -> product.getName().equals("Laptop"));
        boolean hasMouse = result.getProducts().stream()
                .anyMatch(product -> product.getName().equals("Mouse"));

        assertTrue(hasLaptop, "Cart should contain the laptop product");
        assertTrue(hasMouse, "Cart should contain the mouse product");
    }

    @Test
    void addProductToCart_withInvalidCart_shouldThrowException() {
        Product product = new Product("Ice", 49.99);

        UUID nonExistentCartId = UUID.randomUUID();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addProductToCart(nonExistentCartId, product);
        });

        String expectedMessage = "Cart with ID " + nonExistentCartId + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage),
                "Exception message should mention that cart was not found");
    }

    @Test
    void addProductToCart_withValidCart_shouldAddProductToCart() {
        User user = new User("OTA");
        Cart cart = new Cart(user.getId());
        cartService.addCart(cart);

        Product product = new Product("Keyboard", 59.99);

        cartService.addProductToCart(cart.getId(), product);

        Cart updatedCart = cartService.getCartById(cart.getId());

        assertNotNull(updatedCart, "Cart should exist");
        assertFalse(updatedCart.getProducts().isEmpty(), "Cart should not be empty");

        boolean productFound = updatedCart.getProducts().stream()
                .anyMatch(p -> p.getName().equals("Keyboard") &&
                        Math.abs(p.getPrice() - 59.99) < 0.001);

        assertTrue(productFound, "The added product should be found in the cart");
        assertEquals(1, updatedCart.getProducts().size(), "Cart should contain 1 product");
    }

    @Test
    void addProductToCart_whenProductAlreadyExists_shouldThrowError() {
        User user = new User("OTA");
        Cart cart = new Cart(user.getId());

        cartService.addCart(cart);

        Product product = new Product("Tablet", 299.99);

        cartService.addProductToCart(cart.getId(), product);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.addProductToCart(cart.getId(), product);
        });

        String expectedMessage = "Product with ID " + product.getId() + " already exists in cart";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage),
                "Exception message should indicate that the product already exists in the cart");

        Cart updatedCart = cartService.getCartById(cart.getId());
        assertEquals(1, updatedCart.getProducts().size(),
                "Cart should still contain only 1 product");
    }

    @Test
    void deleteProductFromCart_whenCartDoesNotExist_shouldThrowError() {
        UUID nonExistentCartId = UUID.randomUUID();
        Product product = new Product("Smartwatch", 149.99);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteProductFromCart(nonExistentCartId, product);
        });

        String expectedMessage = "Cart with ID " + nonExistentCartId + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage),
                "Exception message should indicate that the cart was not found");
    }

    @Test
    void deleteProductFromCart_whenProductExists_shouldDeleteProductFromCart() {
        User user = new User("OMARRRRR");
        Cart cart = new Cart(user.getId());
        cartService.addCart(cart);

        Product product1 = new Product("Keyboard", 49.99);
        Product product2 = new Product("Mouse", 29.99);
        cartService.addProductToCart(cart.getId(), product1);
        cartService.addProductToCart(cart.getId(), product2);

        Cart cartBeforeDeletion = cartService.getCartById(cart.getId());
        assertEquals(2, cartBeforeDeletion.getProducts().size(), "Cart should have 2 products before deletion");

        cartService.deleteProductFromCart(cart.getId(), product1);

        Cart updatedCart = cartService.getCartById(cart.getId());

        assertEquals(1, updatedCart.getProducts().size(), "Cart should have 1 product after deletion");

        Product remainingProduct = updatedCart.getProducts().get(0);
        assertEquals(product2.getId(), remainingProduct.getId(), "The remaining product should be the one not deleted");
        assertEquals("Mouse", remainingProduct.getName(), "The remaining product should have the correct name");
        assertEquals(29.99, remainingProduct.getPrice(), 0.001, "The remaining product should have the correct price");
    }

    @Test
    void deleteProductFromCart_whenProductDoesNotExist_shouldThrowError() {
        User user = new User("Kareem");
        Cart cart = new Cart(user.getId());
        cartService.addCart(cart);

        Product existingProduct = new Product("Mouse", 29.99);
        Product nonExistentProduct = new Product("Headphones", 59.99);

        cartService.addProductToCart(cart.getId(), existingProduct);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteProductFromCart(cart.getId(), nonExistentProduct);
        });

        String expectedMessage = "Product with ID " + nonExistentProduct.getId() + " not found in cart";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage),
                "Exception message should indicate that the product was not found in the cart");

        Cart updatedCart = cartService.getCartById(cart.getId());
        assertEquals(1, updatedCart.getProducts().size(),
                "Cart should still contain the original product");
        assertEquals(existingProduct.getId(), updatedCart.getProducts().get(0).getId(),
                "Cart should still contain the original product with the same ID");
    }

    @Test
    void deleteCartById_whenCartDoesNotExist_shouldThrowError() {
        UUID nonExistentCartId = UUID.randomUUID();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            cartService.deleteCartById(nonExistentCartId);
        });

        String expectedMessage = "Cart with ID " + nonExistentCartId + " not found";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage),
                "Exception message should indicate that the cart was not found");
    }
    @Test
    void deleteCartById_whenCartExists_shouldDeleteCart() {
        User user = new User("Eng. OT");
        Cart cart = new Cart(user.getId());
        cartService.addCart(cart);

        Cart retrievedCart = cartService.getCartById(cart.getId());
        assertNotNull(retrievedCart, "Cart should exist before deletion");

        cartService.deleteCartById(cart.getId());

        Cart deletedCart = cartService.getCartById(cart.getId());

        assertNull(deletedCart, "Cart should not exist after deletion");
    }

    @Test
    void deleteCartById_withMultipleCarts_shouldOnlyDeleteTargetCart() {
        User user1 = new User("Omar");
        User user2 = new User("Tamer");
        User user3 = new User("Abdelaty");

        Cart cart1 = new Cart(user1.getId());
        Cart cart2 = new Cart(user2.getId());
        Cart cart3 = new Cart(user3.getId());

        Cart savedCart1 = cartService.addCart(cart1);
        Cart savedCart2 = cartService.addCart(cart2);
        Cart savedCart3 = cartService.addCart(cart3);

        UUID cart1Id = savedCart1.getId();
        UUID cart2Id = savedCart2.getId();
        UUID cart3Id = savedCart3.getId();

        int initialCartCount = cartService.getCarts().size();
        assertEquals(3, initialCartCount, "Should have 3 carts initially");

        cartService.deleteCartById(cart2Id);

        ArrayList<Cart> remainingCarts = cartService.getCarts();

        assertEquals(initialCartCount - 1, remainingCarts.size(),
                "Cart count should decrease by exactly 1");

        boolean cart1Exists = remainingCarts.stream()
                .anyMatch(c -> c.getId().equals(cart1Id));
        boolean cart2Exists = remainingCarts.stream()
                .anyMatch(c -> c.getId().equals(cart2Id));
        boolean cart3Exists = remainingCarts.stream()
                .anyMatch(c -> c.getId().equals(cart3Id));

        assertTrue(cart1Exists, "First cart should still exist");
        assertFalse(cart2Exists, "Second cart should be deleted");
        assertTrue(cart3Exists, "Third cart should still exist");
    }

}