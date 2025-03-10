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
    void testAddProduct_WithValidInput_ShouldReturnSuccess() {
        when(productRepository.addProduct(any())).thenReturn(validProduct);
        Product result = productService.addProduct(new Product("Test Product from My Tests", 100.0));
        assertNotNull(result);
        assertEquals("Test Product from My Tests", result.getName());
        assertEquals(100.0, result.getPrice());
    }

    @Test
    void testAddProduct_WithInvalidPrice_ShouldReturnErrorMessage() {
        Product invalidProduct = new Product("Invalid Product", -50.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(invalidProduct);
        });
        assertEquals("Price cannot be negative!", exception.getMessage());
    }

    @Test
    void testAddProduct_WithMissingName_ShouldReturnErrorMessage() {
        Product invalidProduct = new Product(null, 100.0);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.addProduct(invalidProduct);
        });
        assertEquals("Product name cannot be null or empty!", exception.getMessage());
    }

    // Test getProductById
    @Test
    void testGetProductById_WithValidId_ShouldReturnSuccess() {
        when(productRepository.getProductById(validProductId)).thenReturn(validProduct);
        Product result = productService.getProductById(validProductId);
        assertNotNull(result);
        assertEquals(validProductId, result.getId());
    }

    @Test
    void testGetProductById_WithInvalidId_ShouldReturnNotFound() {
        when(productRepository.getProductById(any())).thenReturn(null);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.getProductById(UUID.randomUUID()));
        assertEquals("Product ID not found!", exception.getMessage());
    }

    @Test
    void testGetProductById_WithNullId_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.getProductById(null));
        assertEquals("Product ID not found!", exception.getMessage());
    }

    // Test getProducts
    @Test
    void testGetProducts_WithValidInputs_ShouldReturnSuccess() {
        when(productRepository.getProducts()).thenReturn(new ArrayList<>(Arrays.asList(validProduct)));
        ArrayList<Product> result = productService.getProducts();
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testGetProducts_WithEmptyList_ShouldReturnSuccess() {
        when(productRepository.getProducts()).thenReturn(new ArrayList<>());
        ArrayList<Product> result = productService.getProducts();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetProducts_WithNullResponse_ReturnNothing() {
        when(productRepository.getProducts()).thenReturn(null);
        ArrayList<Product> result = productService.getProducts();
        assertNull(result);
    }

    // Test updateProduct
    @Test
    void testUpdateProduct_WithValidInputs_ShouldReturnSuccess() {
        when(productRepository.updateProduct(validProductId, "Updated Name", 150.0)).thenReturn(new Product(validProductId, "Updated Name", 150.0));
        Product result = productService.updateProduct(validProductId, "Updated Name", 150.0);
        assertNotNull(result);
        assertEquals("Updated Name", result.getName());
        assertEquals(150.0, result.getPrice());
    }

    @Test
    void testUpdateProduct_InvalidNegativePrice_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(validProductId, "Updated Name", -10.0);
        });
        assertEquals("Price cannot be negative!", exception.getMessage());
    }

    @Test
    void testUpdateProduct_InvalidZeroPrice_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(validProductId, "Updated Name", 0.0);
        });
        assertEquals("Price cannot be zero!", exception.getMessage());
    }

    @Test
    void testUpdateProduct_MissingName_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(validProductId, null, 150.0);
        });
        assertEquals("Product name cannot be null or empty!", exception.getMessage());
    }

    // Test deleteProductById
    @Test
    void testDeleteProductById_TestWithValidId_ShouldReturnSuccess() {
        // Arrange: Ensure the product exists before attempting to delete
        when(productRepository.getProductById(validProductId)).thenReturn(validProduct);
        doNothing().when(productRepository).deleteProductById(validProductId);

        // Act & Assert: It should not throw an exception when deleting an existing product
        assertDoesNotThrow(() -> productService.deleteProductById(validProductId));
    }


    @Test
    void testDeleteProductById_InvalidId_ShouldReturnErrorMessage() {
        // Arrange: Make getProductById return null to simulate a non-existing product
        when(productRepository.getProductById(any())).thenReturn(null);

        // Act & Assert: Expect IllegalArgumentException when deleting a non-existent product
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.deleteProductById(UUID.randomUUID()));

        // Assert: Validate the correct error message
        assertEquals("Product not found!", exception.getMessage());
    }

    @Test
    void testDeleteProductById_NullId_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.deleteProductById(null));
        assertEquals("Product ID must not be null!", exception.getMessage());
    }

    // Test applyDiscount
    @Test
    void testApplyDiscount_WithValidInputs_ShouldReturnSuccess() {
        doNothing().when(productRepository).applyDiscount(10.0, new ArrayList<>(Arrays.asList(validProductId)));
        assertDoesNotThrow(() -> productService.applyDiscount(10.0, new ArrayList<>(Arrays.asList(validProductId))));
    }

    @Test
    void testApplyDiscount_WithInvalidPercentage_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.applyDiscount(-50.0, new ArrayList<>(Arrays.asList(validProductId)));
        });
        assertEquals("Discount must be between 0.01% and 99.99%!", exception.getMessage());
    }

    @Test
    void testApplyDiscount_EmptyProductList_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productService.applyDiscount(10.0, new ArrayList<>()));
        assertEquals("Product ID list must not be empty!", exception.getMessage());
    }

    @Test
    void testApplyDiscount_NullDiscount_ShouldReturnErrorMessage() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                productService.applyDiscount(null, new ArrayList<>(Arrays.asList(validProductId)))
        );

        assertEquals("Discount value must not be null!", exception.getMessage());
    }

}

