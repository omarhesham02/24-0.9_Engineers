package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
@SuppressWarnings("rawtypes")
public class CartRepository extends MainRepository<Cart> {

    public CartRepository() {}

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/carts.json";
    }

    @Override
    protected Class<Cart[]> getArrayType() {
        return Cart[].class;
    }

    public Cart addCart(Cart cart){
        save(cart);
        return cart;
    }

    public ArrayList<Cart> getCarts(){
        return findAll();
    }

    public Cart getCartById(UUID id){
        return findAll().stream()
                .filter(cart -> cart.getId().equals(id))
                .findFirst()
                .orElse(null);;
    }

    public Cart getCartByUserId(UUID userId){
        return null;
    }

    public void addProductToCart(UUID cartId, Product product){

    }

    public void deleteProductFromCart(UUID cartId, Product product){

    }

    public void deleteCartById(UUID cartId){

    }
}
