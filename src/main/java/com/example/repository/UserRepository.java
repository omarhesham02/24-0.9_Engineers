package com.example.repository;

import com.example.model.Order;
import com.example.model.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepository extends MainRepository<User> {

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
        return findAll().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public User addUser(User user) {
        save(user);
        return user;
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        User user = getUserById(userId);
        if (user == null) {
            return null;
        }
        return user.getOrders();
    }

    public void addOrderToUser(UUID userId, Order order) {
        User user = getUserById(userId);
        if (user == null) {
            return;
        }

        user.addOrder(order);
        save(user);
    }

    //TODO: Fix removeOrderFromUser method
    public void removeOrderFromUser(UUID userId, UUID orderId) {
        User user = getUserById(userId);
        if (user == null) {
            return;
        }
      //  user.getOrders().removeIf(order -> order.getId().equals(orderId));
    }

    public void deleteUser(UUID userId) throws Exception {
        List<User> users = findAll();
        boolean removed = users.removeIf(user -> user.getId().equals(userId));
        if (!removed) {
            throw new RuntimeException("User with ID " + userId + " not found");
        }
        saveAll(new ArrayList<>(users));
    }
}