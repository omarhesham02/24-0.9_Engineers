package com.example.model;

import interfaces.Identifiable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class User implements Identifiable {
    private UUID id;
    private String name;
    private List<Order> orders = new ArrayList<>();

    public User() {

    }

    public User(UUID userID, String name) {
        this.id = userID;
        this.name = name;
        this.orders = new ArrayList<>();
    }

    public User(UUID id, String name, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.orders = orders;
    }

    public User(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.orders = new ArrayList<>();
    }

    public User(String name, List<Order> orders) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.orders = orders;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public Order getOrderById(UUID orderId) {
        return orders.stream()
                .filter(order -> order.getId().equals(orderId))
                .findFirst()
                .orElse(null);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", orders=" + orders +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) &&
                name.equals(user.name) &&
                orders.equals(user.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, orders);
    }
}