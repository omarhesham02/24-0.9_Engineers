package com.example.MiniProject1;


import com.example.model.Product;
import com.example.repository.ProductRepository;
import com.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product validProduct;
    private UUID validProductId;

    @BeforeEach
    void setUp() {
        validProductId = UUID.randomUUID();
        validProduct = new Product(validProductId, "Test Product from My Tests", 100.0);
    }

    // Test addProduct
    @Test
    void testAddProduct_Success() {
        when(productRepository.addProduct(any())).thenReturn(validProduct);
        Product result = productService.addProduct(new Product("Test Product from My Tests", 100.0));
        assertNotNull(result);
        assertEquals("Test Product from My Tests", result.getName());
        assertEquals(100.0, result.getPrice());
    }

    @Test
    void testAddProduct_InvalidPrice() {
        Product invalidProduct = new Product("Invalid Product", -50.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(invalidProduct);
        });
        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    void testAddProduct_MissingName() {
        Product invalidProduct = new Product(null, 100.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(invalidProduct);
        });
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    // Test getProductById
    @Test
    void testGetProductById_Success() {
        when(productRepository.getProductById(validProductId)).thenReturn(validProduct);
        Product result = productService.getProductById(validProductId);
        assertNotNull(result);
        assertEquals(validProductId, result.getId());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.getProductById(any())).thenReturn(null);
        Product result = productService.getProductById(UUID.randomUUID());
        assertNull(result);
    }

    @Test
    void testGetProductById_NullId() {
        Product result = productService.getProductById(null);
        assertNull(result);
    }

    // Test getProducts
    @Test
    void testGetProducts_Success() {
        when(productRepository.getProducts()).thenReturn(new ArrayList<>(Arrays.asList(validProduct)));
        ArrayList<Product> result = productService.getProducts();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetProducts_EmptyList() {
        when(productRepository.getProducts()).thenReturn(new ArrayList<>());
        ArrayList<Product> result = productService.getProducts();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProducts_NullResponse() {
        when(productRepository.getProducts()).thenReturn(null);
        ArrayList<Product> result = productService.getProducts();
        assertNull(result);
    }

    // Test updateProduct
    @Test
    void testUpdateProduct_Success() {
        when(productRepository.updateProduct(validProductId, "Updated Name", 150.0)).thenReturn(new Product(validProductId, "Updated Name", 150.0));
        Product result = productService.updateProduct(validProductId, "Updated Name", 150.0);
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals(150.0, result.getPrice());
    }

    @Test
    void testUpdateProduct_InvalidPrice() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(validProductId, "Updated Name", -10.0);
        });
        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    void testUpdateProduct_MissingName() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(validProductId, null, 150.0);
        });
        assertEquals("Product name cannot be null or empty", exception.getMessage());
    }

    // Test deleteProductById
    @Test
    void testDeleteProductById_Success() {
        doNothing().when(productRepository).deleteProductById(validProductId);
        assertDoesNotThrow(() -> productService.deleteProductById(validProductId));
    }

    @Test
    void testDeleteProductById_NotFound() {
        doNothing().when(productRepository).deleteProductById(any());
        assertDoesNotThrow(() -> productService.deleteProductById(UUID.randomUUID()));
    }

    @Test
    void testDeleteProductById_NullId() {
        assertDoesNotThrow(() -> productService.deleteProductById(null));
    }

    // Test applyDiscount
    @Test
    void testApplyDiscount_Success() {
        doNothing().when(productRepository).applyDiscount(10.0, new ArrayList<>(Arrays.asList(validProductId)));
        assertDoesNotThrow(() -> productService.applyDiscount(10.0, new ArrayList<>(Arrays.asList(validProductId))));
    }

    @Test
    void testApplyDiscount_InvalidPercentage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.applyDiscount(-50.0, new ArrayList<>(Arrays.asList(validProductId)));
        });
        assertEquals("Discount must be between 0 and 100", exception.getMessage());
    }

    @Test
    void testApplyDiscount_EmptyProductList() {
        doNothing().when(productRepository).applyDiscount(10.0, new ArrayList<>());
        assertDoesNotThrow(() -> productService.applyDiscount(10.0, new ArrayList<>()));
    }
}

