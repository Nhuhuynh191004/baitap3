package com.example.baitap3.service;

import com.example.baitap3.entity.ShoppingCart;
import java.util.List;

public interface ShoppingCartService {
    List<ShoppingCart> getCartByUserId(Integer userId);
    ShoppingCart addProductToCart(Integer userId, Integer productId, Integer quantity);
    ShoppingCart updateQuantity(Integer userId, Integer productId, Integer quantity);
    boolean removeProduct(Integer userId, Integer productId);
    void clearCart(Integer userId);
}
