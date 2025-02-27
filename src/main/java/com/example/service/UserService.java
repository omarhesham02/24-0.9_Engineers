package com.example.service;

import com.example.model.Order;
import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService extends MainService<User> {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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


    // TODO: Implement aaddOrderToUser(UUID userId, Order order) method
    public void addOrderToUser(UUID userId, Order order) {
    }

    // TODO: Implement emptyCart(UUID userId) method
    public void emptyCart(UUID userId) {
    }

    public void removeOrderFromUser(UUID userId, UUID orderId) {
        userRepository.removeOrderFromUser(userId, orderId);
    }

    public void deleteUserById(UUID userId) throws Exception {
        userRepository.deleteUser(userId);
    }
}