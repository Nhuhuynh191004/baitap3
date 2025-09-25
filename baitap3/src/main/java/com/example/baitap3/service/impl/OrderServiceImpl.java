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
    public Optional<Order> getOrderById(Integer orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public Order createOrder(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<ShoppingCart> cartList = cartRepository.findByUserId(userId);

        if (cartList.isEmpty()) throw new RuntimeException("Cart is empty");

        Order order = Order.builder()
                .user(user)
                .orderDate(LocalDateTime.now())
                .status("Pending")
                .build();
        order = orderRepository.save(order);

        for (ShoppingCart cart : cartList) {
            Product product = cart.getProduct();
            if (product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            // Tạo order detail
            OrderDetail orderDetail = OrderDetail.builder()
                    .order(order)
                    .product(product)
                    .quantity(cart.getQuantity())
                    .unitPrice(product.getPrice())
                    .build();
            orderDetailRepository.save(orderDetail);

            // Trừ stock
            product.setStock(product.getStock() - cart.getQuantity());
            productRepository.save(product);
        }

        // Xóa giỏ hàng
        cartRepository.deleteAll(cartList);

        return order;
    }

    @Override
    public Order updateOrderStatus(Integer orderId, String status) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
