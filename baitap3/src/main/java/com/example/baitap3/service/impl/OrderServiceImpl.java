package com.example.baitap3.service.impl;

import com.example.baitap3.entity.*;
import com.example.baitap3.repository.*;
import com.example.baitap3.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserRepository userRepository;
    private final ShoppingCartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;

    @Override
    public List<Order> getOrdersByUserId(Integer userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Order createOrder(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("User not found"));

        List<ShoppingCart> cartList = cartRepository.findByUserId(userId);
        if (cartList.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Tạo order mới
        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status("PENDING")
                .build();
        order = orderRepository.save(order);

        // Tạo order detail từ cart
        for (ShoppingCart cart : cartList) {
            Product product = cart.getProduct();
            if (product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(cart.getQuantity())
                    .unit_price(product.getPrice())
                    .build();
            orderDetailRepository.save(orderDetail);

            // Trừ stock
            product.setStock(product.getStock() - cart.getQuantity());
            productRepository.save(product);
        }

        // Xóa giỏ hàng sau khi đặt
        cartRepository.deleteAll(cartList);

        return order;
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String status, Integer userId, boolean isAdmin) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (isAdmin) {
            // Admin không được update status
            throw new RuntimeException("Admin cannot update order status");
        } else {
            // Chỉ owner của order mới được update
            if (!order.getUser().getId().equals(userId)) {
                throw new RuntimeException("You do not own this order");
            }

            // User chỉ được đổi sang "DELIVERED"
            if (!"DELIVERED".equalsIgnoreCase(status)) {
                throw new RuntimeException("User can only set status to DELIVERED");
            }

            order.setStatus("DELIVERED");
        }

        return orderRepository.save(order);
    }

    @Override
    public Integer getUserIdByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public boolean isOrderOwnedByUser(Integer orderId, Integer userId) {
        return orderRepository.findById(orderId)
                .map(order -> order.getUser().getId().equals(userId))
                .orElse(false);
    }
}
