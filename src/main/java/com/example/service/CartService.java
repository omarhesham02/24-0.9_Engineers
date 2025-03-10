package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class CartService extends MainService<Cart> {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart addCart(Cart cart){
        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }

        Cart existingCart = cartRepository.getCartById(cart.getId());

        if (existingCart != null) {
            throw new IllegalArgumentException("Cart with this ID already exists");
        }

        cartRepository.addCart(cart);
        return cart;
    }

    public ArrayList<Cart> getCarts(){
        return cartRepository.getCarts();
    }

    public Cart getCartById(UUID cartId) {
        if (cartId == null) {
           throw new IllegalArgumentException("cartId cannot be null");
        }

        return cartRepository.getCartById(cartId);
    }

    public Cart getCartByUserId(UUID userId){
        return cartRepository.getCartByUserId(userId);
    }

    public void addProductToCart(UUID userId, Product product) {

        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }

        Cart cart = cartRepository.getCartByUserId(userId);

        if (cart == null) {
            cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
            cartRepository.addCart(cart);
        }

        cartRepository.addProductToCart(cart.getId(), product);
    }

    public void deleteProductFromCart(UUID cartId, Product product) {

        Cart cart = cartRepository.getCartById(cartId);

        if (cart == null || cart.getProducts().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        if (!cart.getProducts().contains(product)) {
            throw new IllegalArgumentException("Product is not in the cart");
        }

        cartRepository.deleteProductFromCart(cartId, product);
    }

    public void deleteCartById(UUID cartId){
        cartRepository.deleteCartById(cartId);
    }

}
