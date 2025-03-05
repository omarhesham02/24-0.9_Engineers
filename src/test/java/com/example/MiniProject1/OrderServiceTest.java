package com.example.MiniProject1;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.service.OrderService;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private OrderService orderService;


    @Test
    void addOrder() {
    }

    @Test
    void getOrders() {
    }

    @Test
    void getOrderById() {
    }

    @Test
    void deleteOrderById() {
    }
}