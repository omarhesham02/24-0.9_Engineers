package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
@SuppressWarnings("rawtypes")
public class ProductService extends MainService<Product> {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public Product addProduct(Product product){
        return productRepository.addProduct(product);
    }

    public ArrayList<Product> getProducts() {
        return productRepository.getProducts();
    }

    public Product getProductById(UUID productId) {
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
