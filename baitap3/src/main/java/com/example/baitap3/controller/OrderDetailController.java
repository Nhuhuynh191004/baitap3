package com.example.baitap3.controller;

import com.example.baitap3.entity.OrderDetail;
import com.example.baitap3.service.OrderDetailService;
import com.example.baitap3.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/order-details")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;
    private final OrderService orderService;

    // USER: chỉ xem chi tiết order của mình
    // ADMIN: xem chi tiết order bất kỳ
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderDetail>> getOrderDetails(
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

        return ResponseEntity.ok(orderDetailService.getOrderDetailsByOrderId(orderId));
    }
}
