package com.example.MiniProject1;


import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductServiceTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductService productService;

    private static ArrayList<Product> productsJSON;

    private Product validProduct;
    private UUID validProductId;

    @BeforeEach
    void setUp() {
        validProductId = UUID.randomUUID();
        validProduct = new Product(validProductId, "Test Product from My Tests", 100.0);
        productService.addProduct(validProduct);
    }


    @BeforeAll
    void backupData() {
        productsJSON = productService.getProducts();

    }

    @AfterAll
    void restoreData() {
        productRepository.saveAll(productsJSON);
    }



    // Test addProduct
    @Test
    void addProduct_WithValidInput_ShouldReturnSuccess() {

        Product product = productService.getProductById(validProductId);

        assertNotNull(product);
        assertEquals(validProduct.getId(), product.getId());

    }

    @Test
    void addProduct_WithInvalidPrice_ShouldReturnErrorMessage() {
        Product invalidProduct = new Product("Invalid Product", -50.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.addProduct(invalidProduct));
        assertEquals("Price cannot be negative!", exception.getMessage());
    }

    @Test
    void addProduct_WithMissingName_ShouldReturnErrorMessage() {
        Product invalidProduct = new Product(null, 100.0);

        Exception exception = assertThrows(HttpClientErrorException.class, () -> productService.addProduct(invalidProduct));

        // Extract only the error message (excluding status code)
        String actualMessage = exception.getMessage().split(" ", 2)[1];

        assertEquals("Product name cannot be null or empty!", actualMessage);
    }


    // Test getProductById
    @Test
    void getProductById_WithValidId_ShouldReturnSuccess() {

        Product product = productService.getProductById(validProductId);

        assertNotNull(product);
        assertEquals(validProduct.getId(), product.getId());
    }

    @Test
    void getProductById_WithInvalidId_ShouldReturnNull() {
        UUID invalidId = UUID.randomUUID();

        assertNull(productService.getProductById(invalidId));
    }

    @Test
    void getProductById_WithNullId_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.getProductById(null));
        assertEquals("Product ID cannot be null!", exception.getMessage());
    }

    // Test getProducts
    @Test
    void getProducts_WithValidInputs_ShouldReturnSuccess() {
        Product product1 = new Product(UUID.randomUUID(), "Product 1", 100.0);
        Product product2 = new Product(UUID.randomUUID(), "Product 2", 200.0);
        Product product3 = new Product(UUID.randomUUID(), "Product 3", 300.0);

        productService.addProduct(product1);
        productService.addProduct(product2);
        productService.addProduct(product3);

        ArrayList<Product> result = productService.getProducts();

        assertNotNull(result);
        assertTrue(result.contains(product1));
        assertTrue(result.contains(product2));
        assertTrue(result.contains(product3));
    }

    @Test
    void getProducts_WithEmptyList_ShouldReturnSuccess() {
        // Clear all products
        productRepository.saveAll(new ArrayList<>());

        ArrayList<Product> result = productService.getProducts();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    // Test updateProduct
    @Test
    void updateProduct_WithValidInputs_ShouldReturnSuccess() {

        String updatedName = "Updated Name";
        double updatedPrice = 150.0;

        Product updatedProduct = productService.updateProduct(validProductId, updatedName, updatedPrice);

        assertNotNull(updatedProduct);
        assertEquals(validProduct.getId(), updatedProduct.getId());
        assertEquals(updatedName, updatedProduct.getName());
        assertEquals(updatedPrice, updatedProduct.getPrice());

    }

    @Test
    void updateProduct_InvalidNegativePrice_ShouldReturnErrorMessage() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(validProductId, "Updated Name", -50.0));

        assertEquals("Price cannot be negative!", exception.getMessage());

    }

    @Test
    void updateProduct_InvalidZeroPrice_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(validProductId, "Updated Name", 0.0));
        assertEquals("Price cannot be zero!", exception.getMessage());
    }


    @Test
    void updateProduct_MissingName_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.updateProduct(validProductId, null, 150.0));
        assertEquals("Product name cannot be null or empty!", exception.getMessage());
    }

    // Test deleteProductById
    @Test
    void deleteProductById_TestWithValidId_ShouldReturnSuccess() {

        productService.deleteProductById(validProductId);

        Product product = productService.getProductById(validProductId);

        assertNull(product);
    }


    @Test
    void deleteProductById_InvalidId_ShouldReturnErrorMessage() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.deleteProductById(UUID.randomUUID()));

        assertEquals("Product not found!", exception.getMessage());
    }

    @Test
    void deleteProductById_NullId_ShouldReturnErrorMessage() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.deleteProductById(null));

        assertEquals("Product ID must not be null!", exception.getMessage());

    }

    // Test applyDiscount
    @Test
    void applyDiscount_WithValidInputs_ShouldReturnSuccess() {

        productService.applyDiscount(10.0, new ArrayList<>(Collections.singletonList(validProductId)));

        Product product = productService.getProductById(validProductId);

        assertNotNull(product);
        assertEquals(90.0, product.getPrice());
    }

    @Test
    void applyDiscount_WithInvalidPercentage_ShouldReturnErrorMessage() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.applyDiscount(-10.0, new ArrayList<>(Collections.singletonList(validProductId))));

        assertEquals("Discount value must be between 0% and 100%!", exception.getMessage());

    }

    @Test
    void applyDiscount_EmptyProductList_ShouldReturnErrorMessage() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.applyDiscount(10.0, new ArrayList<>()));

        assertEquals("Product ID list must not be empty!", exception.getMessage());
    }

    @Test
    void applyDiscount_NullDiscount_ShouldReturnErrorMessage() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.applyDiscount(null, new ArrayList<>(Collections.singletonList(validProductId))));

        assertEquals("Discount value must not be null!", exception.getMessage());
    }

}

