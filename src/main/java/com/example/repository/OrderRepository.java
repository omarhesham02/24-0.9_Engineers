package com.example.repository;

import com.example.model.Order;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OrderRepository extends MainRepository<Order> {

    @Value("${spring.application.orderDataPath}")
    private String orderDataPath;

    @Override
    protected String getDataPath() {
        return orderDataPath;
    }
    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class;
    }


    public OrderRepository() { }

    public void addOrder(Order order) {
        save(order);
    }

    public ArrayList<Order> getOrders(){
        return findAll();
    }

    public Order getOrderById(UUID orderId){
        return findById(orderId);
    }

    public List<Order> getOrdersByUserId(UUID userId) {
        return findAll().stream()
                .filter(order -> order.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    public void deleteOrderById(UUID orderId){
        deleteById(orderId);
    }
}
