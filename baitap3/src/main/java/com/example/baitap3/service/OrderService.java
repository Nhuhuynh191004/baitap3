package com.example.baitap3.service;

import com.example.baitap3.entity.Order;
import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getOrdersByUserId(Integer userId);
    Order createOrder(Integer userId);
    Optional<Order> getOrderById(Integer orderId);
    Order updateOrderStatus(Integer orderId, String status);
}
