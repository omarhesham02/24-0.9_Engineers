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

<<<<<<< HEAD
    public Product addProduct(Product product) {
        if (product.getId() == null) {
            product.setId(UUID.fromString(UUID.randomUUID().toString()));
        }
        save(product);
        return product;
    }

    public ArrayList<Product> getProducts() {
        return findAll();
    }

    public Product getProductById(UUID productId) {
        return findAll().stream()
                .filter(product -> product.getId() != null && product.getId().equals(productId))
                .findFirst()
                .orElse(null);
=======
    public Product addProduct(Product product){
        save(product);
        return product;
    }
    public ArrayList<Product> getProducts(){
        return findAll();
    }
    public Product getProductById(UUID productId){
        return findById(productId);
>>>>>>> ffe50a8641b8ebc09371422ee058fa17fff7079d
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
<<<<<<< HEAD

    public void applyDiscount(double discount, ArrayList<UUID> productIds) {
        ArrayList<Product> products = findAll();
        for (Product product : products) {
            if (product.getId() != null && productIds.contains(product.getId())) {
                double newPrice = product.getPrice() * (1 - (discount / 100));
                product.setPrice(newPrice);
            }
        }
        overrideData(products);
=======
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
>>>>>>> ffe50a8641b8ebc09371422ee058fa17fff7079d
    }

    public void deleteProductById(UUID productId) {
        ArrayList<Product> products = findAll();
        products.removeIf(product -> product.getId() != null && product.getId().equals(productId));
        overrideData(products);
    }
}
