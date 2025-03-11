package com.example.repository;

import com.example.model.Order;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.UUID;

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

    public void deleteOrderById(UUID orderId){
        ArrayList<Order> orders = findAll();

        boolean removed = orders.removeIf(order -> order.getId().equals(orderId));

        if (!removed) {
            throw new IllegalArgumentException("Order not found");
        }

        saveAll(orders);
    }
}
