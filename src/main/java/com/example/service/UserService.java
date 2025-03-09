package com.example.service;

import com.example.model.Order;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends MainService<User> {

    private final UserRepository userRepository;
    private final CartService cartService;

    @Autowired
    public UserService(UserRepository userRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    public User addUser(User user) {
        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User cannot be null");
        }
        userRepository.addUser(user);
        return user;
    }

    public ArrayList<User> getUsers() {
        return (ArrayList<User>) userRepository.getAllUsers();
    }

    public User getUserById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.getUserById(id);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository.getUserById(userId);

        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }

        return userRepository.getOrdersByUserId(userId);
    }

    public void addOrderToUser(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        // userRepository.addOrderToUser(userId, order);
    }

    public void emptyCart(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        cartService.deleteCartById(userId);
    }

    // TODO: Why is .getOrderById not finding the order?!
    public void removeOrderFromUser(UUID userId, UUID orderId) {

        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        User user = userRepository.getUserById(userId);
        Order order = userRepository.getOrderById(userId, orderId);

        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }

        if (order == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "Order not found");
        }

        userRepository.removeOrderFromUser(userId, orderId);
    }

    public void deleteUserById(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        User user = userRepository.getUserById(userId);

        if (user == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found");
        }
        userRepository.deleteUser(userId);
    }
}