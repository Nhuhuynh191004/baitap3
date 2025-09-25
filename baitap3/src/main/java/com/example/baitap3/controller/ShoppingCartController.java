package com.example.baitap3.controller;

import com.example.baitap3.entity.ShoppingCart;
import com.example.baitap3.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    // DTO cho request thêm/cập nhật
    public static record CartRequest(Integer productId, Integer quantity) {}

    /**
     * Lấy danh sách sản phẩm trong giỏ của user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ShoppingCart>> getCart(@PathVariable Integer userId) {
        List<ShoppingCart> cartItems = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    /**
     * Thêm sản phẩm vào giỏ
     */
    @PostMapping("/user/{userId}/product")
    public ResponseEntity<?> addToCart(@PathVariable Integer userId,
                                       @RequestBody CartRequest request) {
        ShoppingCart cartItem = cartService.addProductToCart(userId, request.productId(), request.quantity());
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User hoặc Product không tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    @PutMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<?> updateQuantity(@PathVariable Integer userId,
                                            @PathVariable Integer productId,
                                            @RequestBody CartRequest request) {
        ShoppingCart cartItem = cartService.updateQuantity(userId, productId, request.quantity());
        if (cartItem == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Cart item không tồn tại");
        }
        return ResponseEntity.ok(cartItem);
    }

    /**
     * Xóa một sản phẩm trong giỏ
     */
    @DeleteMapping("/user/{userId}/product/{productId}")
    public ResponseEntity<Void> removeProduct(@PathVariable Integer userId,
                                              @PathVariable Integer productId) {
        boolean removed = cartService.removeProduct(userId, productId);
        if (!removed) {
            return ResponseEntity.notFound().build(); // 404 nếu không tìm thấy
        }
        return ResponseEntity.noContent().build();    // 204 nếu xóa thành công
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Integer userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build(); // 204
    }
}
