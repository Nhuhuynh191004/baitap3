package com.example.baitap3.controller;

import com.example.baitap3.entity.Order;
import com.example.baitap3.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // USER: xem đơn hàng của mình
    @GetMapping("/my")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Integer userId = orderService.getUserIdByEmail(userDetails.getUsername());
        return ResponseEntity.ok(orderService.getOrdersByUserId(userId));
    }

    // ADMIN: xem tất cả order
    @GetMapping("/admin")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // USER: checkout
    @PostMapping("/checkout")
    public ResponseEntity<?> checkout(Authentication authentication) {
        if (authentication == null) {
            return ResponseEntity.status(401).body("Bạn chưa đăng nhập");
        }

        String email = authentication.getName();
        Integer userId = orderService.getUserIdByEmail(email);

        if (userId == null) {
            return ResponseEntity.badRequest().body("Không tìm thấy user với email: " + email);
        }

        try {
            Order order = orderService.createOrder(userId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            e.printStackTrace(); // in log server
            return ResponseEntity.status(500).body("Checkout thất bại: " + e.getMessage());
        }
    }


    // USER + ADMIN: xem chi tiết order
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(
            @PathVariable Integer orderId,
            Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_Admin"));

        if (!isAdmin) {
            Integer userId = orderService.getUserIdByEmail(email);
            if (!orderService.isOrderOwnedByUser(orderId, userId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return orderService.getOrderById(orderId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // USER: xác nhận đã nhận hàng
    // ADMIN: cập nhật trạng thái bất kỳ
    @PutMapping("/{orderId}/status")
    public ResponseEntity<Order> updateStatus(
            @PathVariable Integer orderId,
            @RequestParam String status,
            Authentication authentication) {

        String email = authentication.getName();
        Integer userId = orderService.getUserIdByEmail(email);

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_Admin"));

        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status, userId, isAdmin));
    }
}
