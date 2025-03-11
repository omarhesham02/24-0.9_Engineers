package com.example.repository;

import com.example.model.Cart;
import com.example.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

@Repository
public class CartRepository extends MainRepository<Cart> {

    @Value("${spring.application.cartDataPath}")
    private String cartDataPath;

    @Override
    protected String getDataPath() {
        return cartDataPath;
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
        return findById(id);
    }


    public Cart getCartByUserId(UUID userId) {
        Cart existingCart = findAll().stream()
                .filter(cart -> cart.getUserId().equals(userId))
                .findFirst()
                .orElse(null);

        if (existingCart != null) {
            return existingCart;
        }

        Cart newCart = new Cart(userId);
        return addCart(newCart);
    }

    // There is possibly two approaches, one is to allow duplicates (since there is no
    // indicator of the amount of a product), or to only allow for one instance of a
    // product to be in the cart at a given time. The following method does not allow for duplicates.
    public void addProductToCart(UUID cartId, Product product) {
        Cart cart = getCartById(cartId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart with ID " + cartId + " not found");
        }
        boolean productExists = cart.getProducts().contains(product);
        if (productExists) {
            throw new IllegalArgumentException("Product with ID " + product.getId() + " already exists in cart");
        }

        cart.getProducts().add(product);
        override(cart);
    }

    public void deleteProductFromCart(UUID cartId, Product product) {
        Cart cart = getCartById(cartId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart with ID " + cartId + " not found");
        }

        boolean productExists = cart.getProducts().removeIf(p -> p.getId().equals(product.getId()));
        if (!productExists) {
            throw new IllegalArgumentException("Product with ID " + product.getId() + " not found in cart");
        }

        override(cart);
    }

    public void deleteCartById(UUID cartId){
        deleteById(cartId);
    }
}
