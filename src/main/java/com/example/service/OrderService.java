package com.example.service;

import com.example.model.Order;
import com.example.repository.OrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
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
        orderRepository.addOrder(order);
    }

    public ArrayList<Order> getOrders(){
        return orderRepository.getOrders();
    }

    public Order getOrderById(UUID orderId){
        return orderRepository.getOrderById(orderId);
    }

    public void deleteOrderById(UUID orderId) throws IllegalArgumentException {

        if (orderRepository.getOrderById(orderId) == null) {
            throw new IllegalArgumentException("Order not found");
        }

        Order order = orderRepository.getOrderById(orderId);

        if (order == null) {
            throw new HttpStatusCodeException(HttpStatus.NOT_FOUND, "Order not found") {};
        }

        orderRepository.deleteOrderById(orderId);
    }

    public void clearAll() {
        orderRepository.clearAll();
    }
}
