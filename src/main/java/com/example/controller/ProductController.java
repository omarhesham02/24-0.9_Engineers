package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @GetMapping("/")
    public ResponseEntity<ArrayList<Product>> getProducts() {
        return ResponseEntity.ok(productService.getProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable UUID productId) {
        Product product = productService.getProductById(productId);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID productId, @RequestBody Map<String, Object> body) {
        String newName = (String) body.get("newName");
        double newPrice = ((Number) body.get("newPrice")).doubleValue();
        return ResponseEntity.ok(productService.updateProduct(productId, newName, newPrice));
    }

    @PutMapping("/applyDiscount")
    public ResponseEntity<String> applyDiscount(@RequestParam Double discount, @RequestBody ArrayList<UUID> productIds) {
        if (discount == null) {
            return ResponseEntity.badRequest().body("Discount value must not be null!");
        }
        if (productIds == null || productIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Product ID list must not be empty!");
        }
        productService.applyDiscount(discount, productIds);
        return ResponseEntity.ok("Discount applied successfully");
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.ok("Product deleted successfully");
    }
}
