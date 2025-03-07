package com.example.service;

import com.example.model.Cart;
import com.example.model.Order;
import com.example.model.Product;
import com.example.model.User;
import com.example.repository.CartRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends MainService<User> {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;

    @Autowired
    public UserService(UserRepository userRepository, CartRepository cartRepository) {
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    public User addUser(User user) {
        userRepository.addUser(user);
        return user;
    }

    public ArrayList<User> getUsers() {
        return (ArrayList<User>) userRepository.getAllUsers();
    }

    public User getUserById(UUID id) {
        return userRepository.getUserById(id);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return userRepository.getOrdersByUserId(userId);
    }


    // TODO: Check addOrderToUser(UUID userId) method
    public void addOrderToUser(UUID userId) {
        User user = userRepository.getUserById(userId);
        // draft
        Cart cart = cartRepository.getCartByUserId(userId);
        Order order = new Order(userId,
                cart.getProducts().stream().mapToDouble(Product::getPrice).sum(),
                cart.getProducts());

        emptyCart(userId);
        userRepository.addOrderToUser(userId, order);
    }

    // TODO: Check emptyCart(UUID userId) method
    public void emptyCart(UUID userId) {
        Cart cart = cartRepository.getCartByUserId(userId);
        for (Product product : cart.getProducts())
            cartRepository.deleteProductFromCart(cart.getId(), product);
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
    }

    public void deleteUserById(UUID userId) {
        userRepository.deleteUser(userId);
        Cart cart = cartRepository.getCartByUserId(userId);
        cartRepository.deleteCartById(cart.getId());
    }
}