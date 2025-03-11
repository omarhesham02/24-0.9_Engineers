package com.example.repository;

import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public class ProductRepository extends MainRepository<Product> {
    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/products.json";
    }



    @Override
    protected Class<Product[]> getArrayType() {
        return Product[].class;
    }

    public Product addProduct(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.randomUUID());
        }
        save(product);
        return product;
    }

    public ArrayList<Product> getProducts(){
        return findAll();
    }
    public Product getProductById(UUID productId){
        return findById(productId);
    }

    public Product updateProduct(UUID productId, String newName, double newPrice) {
        ArrayList<Product> products = findAll();
        for (Product product : products) {
            if (product.getId() != null && product.getId().equals(productId)) {
                product.setName(newName);
                product.setPrice(newPrice);
                overrideData(products);
                return product;
            }
        }
        return null;
    }
    public void applyDiscount(double discount, ArrayList<UUID> productIds) {

        for (UUID productId : productIds) {
            Product product = getProductById(productId);
            if (product == null) {
                throw new IllegalArgumentException("Product with ID " + productId + " not found");
            }
            double newPrice = product.getPrice() * (1 - discount / 100);
            product.setPrice(newPrice);
            override(product);
        }
    }

    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll();
        products.removeIf(product -> product.getId() != null && product.getId().equals(productId));
        overrideData(products);
    }
}
