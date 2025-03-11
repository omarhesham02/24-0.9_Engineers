package com.example.service;

import com.example.model.Cart;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class CartService extends MainService<Cart> {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartService(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    public Cart addCart(Cart cart) {

        if (cart == null) {
            throw new IllegalArgumentException("Cart cannot be null");
        }

        UUID userId = cart.getUserId();

        if (userId == null) {
            throw new IllegalArgumentException("Cart's User ID cannot be null");
        }

        User user = userRepository.getUserById(userId);

        if (user == null) {
            throw new IllegalArgumentException("Cannot add a cart to a nonexistent user. User with ID " + userId + " not found");
        }

        return cartRepository.addCart(cart);
    }

    public ArrayList<Cart> getCarts() {
        return cartRepository.getCarts();
    }

    public Cart getCartById(UUID cartId) {

        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }

        return cartRepository.getCartById(cartId);
    }

    public Cart getCartByUserId(UUID userId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        return cartRepository.getCartByUserId(userId);
    }

    public void addProductToCart(UUID cartId, Product product) {

        if (cartId == null || product == null) {
            throw new IllegalArgumentException("Cart ID and Product cannot be null");
        }

        cartRepository.addProductToCart(cartId, product);
    }

    public void deleteProductFromCart(UUID cartId, Product product) {

        if (cartId == null || product == null) {
            throw new IllegalArgumentException("Cart ID and Product cannot be null");
        }

        Cart cart = cartRepository.getCartById(cartId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart with ID " + cartId + " not found");
        }

        cartRepository.deleteProductFromCart(cartId, product);
    }

    public void deleteCartById(UUID cartId) {

        if (cartId == null) {
            throw new IllegalArgumentException("Cart ID cannot be null");
        }

        Cart cart = cartRepository.getCartById(cartId);

        if (cart == null) {
            throw new IllegalArgumentException("Cart with ID " + cartId + " not found");
        }

        cartRepository.deleteCartById(cartId);
    }

}
