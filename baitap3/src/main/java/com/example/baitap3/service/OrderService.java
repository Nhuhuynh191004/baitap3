package com.example.baitap3.service;

import com.example.baitap3.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> getOrdersByUserId(Integer userId);
    List<Order> getAllOrders();  // Admin
    Optional<Order> getOrderById(Integer orderId);
    Order createOrder(Integer userId);
    Order updateOrderStatus(Integer orderId, String status, Integer userId, boolean isAdmin);
    Integer getUserIdByEmail(String email);
    boolean isOrderOwnedByUser(Integer orderId, Integer userId);
}
