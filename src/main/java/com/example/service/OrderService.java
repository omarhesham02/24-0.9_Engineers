package com.example.service;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class OrderService extends MainService<Order> {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void addOrder(Order order){
        if (order == null) throw new IllegalArgumentException("Order cannot be null");
        if (order.getUserId() == null) throw new IllegalArgumentException("UserId cannot be null");
        orderRepository.addOrder(order);
    }

    public ArrayList<Order> getOrders(){
        return orderRepository.getOrders();
    }

    public Order getOrderById(UUID orderId){
        if (orderId == null) throw new IllegalArgumentException("OrderId cannot be null");
        return orderRepository.getOrderById(orderId);
    }

    public void deleteOrderById(UUID orderId) throws IllegalArgumentException {
        if (orderId == null)
            throw new IllegalArgumentException("Order not found");

        Order order = orderRepository.getOrderById(orderId);

        if (order == null) {
            throw new HttpStatusCodeException(HttpStatus.NOT_FOUND, "Order not found") {};
        }

        orderRepository.deleteOrderById(orderId);
    }
}
