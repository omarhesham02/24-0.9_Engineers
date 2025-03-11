package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class ProductService extends MainService<Product> {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product addProduct(Product product) {


        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null!");
        }

        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }

        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Product name cannot be null or empty!");
        }

        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative!");
        }

        if (product.getPrice() == 0){
            throw new IllegalArgumentException("Price cannot be zero!");
        }

        Product existingProduct = productRepository.getProductById(product.getId());

        if (existingProduct != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Product with this ID already exists");
        }

        return productRepository.addProduct(product);
    }


    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    public Product getProductById(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null!");
        }

        return productRepository.getProductById(productId);
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be null or empty!");
        }
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative!");
        }
        if (newPrice == 0){
            throw new IllegalArgumentException("Price cannot be zero!");
        }
        return productRepository.updateProduct(productId, newName, newPrice);
    }

    public void deleteProductById(UUID productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null!");
        }
        Product product = productRepository.getProductById(productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found!");
        }
        productRepository.deleteProductById(productId);
    }

    public void applyDiscount(Double discount, ArrayList<UUID> productIds) {
        if (discount == null) {
            throw new IllegalArgumentException("Discount value must not be null!");
        }
        if (discount < 0 || discount > 100) {
            throw new IllegalArgumentException("Discount value must be between 0% and 100%!");
        }
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("Product ID list must not be empty!");
        }
        productRepository.applyDiscount(discount, productIds);
    }
}
