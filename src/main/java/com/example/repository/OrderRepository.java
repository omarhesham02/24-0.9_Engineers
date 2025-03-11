package com.example.repository;

import com.example.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class OrderRepository extends MainRepository<Order> {

    public OrderRepository() {
    }

    @Override
    protected String getDataPath() {
        return "src/main/java/com/example/data/orders.json";
    }
    @Override
    protected Class<Order[]> getArrayType() {
        return Order[].class;
    }


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
