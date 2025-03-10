package com.example.repository;

import com.example.model.Product;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class ProductRepository extends MainRepository<Product> {

    public ProductRepository() {
    }

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/products.json";
    }



    @Override
    protected Class<Product[]> getArrayType() {
        return null;
    }


    public Product addProduct(Product product){
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
        return null;
    }
    public void applyDiscount(double discount, ArrayList<UUID> productIds){

    }
    public void deleteProductById(UUID productId){

    }
}
