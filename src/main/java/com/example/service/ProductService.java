package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class ProductService extends MainService<Product> {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product addProduct(Product product) {
        if (product.getId() == null || product.getName() == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Product cannot be null");
        }

        Product existingProduct = productRepository.getProductById(product.getId());

        if (existingProduct != null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Product with this ID already exists");
        }

        productRepository.addProduct(product);
        return product;
    }

    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    public Product getProductById(UUID productId) {

        if (productId == null) {
            throw new IllegalArgumentException("Product ID cannot be null");
        }

        return productRepository.getProductById(productId);
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        return productRepository.updateProduct(productId, newName, newPrice);
    }

    public String applyDiscount(double discount, ArrayList<UUID> productIds) {
//        return productRepository.applyDiscount(discount, productIds);
        return null;
    }

    public void deleteProductById(UUID productId) {
        productRepository.deleteProductById(productId);
    }
}
