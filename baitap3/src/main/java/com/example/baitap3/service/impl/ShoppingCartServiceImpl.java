package com.example.baitap3.service.impl;

import com.example.baitap3.entity.Product;
import com.example.baitap3.entity.ShoppingCart;
import com.example.baitap3.entity.User;
import com.example.baitap3.repository.ProductRepository;
import com.example.baitap3.repository.ShoppingCartRepository;
import com.example.baitap3.repository.UserRepository;
import com.example.baitap3.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ShoppingCart> getCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId);
    }

    @Override
    public ShoppingCart addProductToCart(Integer userId, Integer productId, Integer quantity) {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(productId).orElseThrow();

        // Kiểm tra sản phẩm đã có trong giỏ
        return cartRepository.findByUserIdAndProductId(userId, productId)
                .map(cart -> {
                    cart.setQuantity(cart.getQuantity() + quantity);
                    return cartRepository.save(cart);
                })
                .orElseGet(() -> {
                    ShoppingCart cart = ShoppingCart.builder()
                            .user(user)
                            .product(product)
                            .quantity(quantity)
                            .build();
                    return cartRepository.save(cart);
                });
    }

    @Override
    public ShoppingCart updateQuantity(Integer userId, Integer productId, Integer quantity) {
        return cartRepository.findByUserIdAndProductId(userId, productId)
                .map(cart -> {
                    cart.setQuantity(quantity);
                    return cartRepository.save(cart);
                })
                .orElse(null); // Controller sẽ check null để trả về 404
    }

    @Override
    public boolean removeProduct(Integer userId, Integer productId) {
        return cartRepository.findByUserIdAndProductId(userId, productId)
                .map(cart -> {
                    cartRepository.delete(cart);
                    return true;
                })
                .orElse(false);
    }

    @Override
    public void clearCart(Integer userId) {
        List<ShoppingCart> cartList = cartRepository.findByUserId(userId);
        cartRepository.deleteAll(cartList);
    }
}
