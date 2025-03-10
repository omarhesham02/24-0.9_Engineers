package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository extends MainRepository<User> {

    private final OrderRepository orderRepository;

    public UserRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/users.json";
    }

    @Override
    protected Class<User[]> getArrayType() {
        return User[].class;
    }

    public List<User> getAllUsers() {
        return findAll();
    }

    public User getUserById(UUID id) {
        return findById(id);
    }

    public User addUser(User user) {
        save(user);
        return user;
    }

    public List<Order> getOrdersByUserId(UUID userId) throws HttpClientErrorException {
        User user = getUserById(userId);
        return user.getOrders();
    }

    public void addOrderToUser(UUID userId, Order order) {
        User user = getUserById(userId);
        user.addOrder(order);
        orderRepository.addOrder(order);
        override(user);
    }


    public void removeOrderFromUser(UUID userId, UUID orderId) throws HttpClientErrorException {
        User user = getUserById(userId);
        Order order = user.getOrderById(orderId);
        user.removeOrder(order);
        override(user);
    }

    public void deleteUser(UUID userId) throws HttpClientErrorException {
        ArrayList<User> users = findAll();
        users.removeIf(user -> user.getId().equals(userId));
        saveAll(users);
    }

    public void deleteAllUsers() {
        saveAll(new ArrayList<>());
    }


    public Order getOrderById(UUID userId, UUID orderId) {
        User user = getUserById(userId);
        return user.getOrderById(orderId);
    }
}