package com.example.baitap3.repository;

import com.example.baitap3.entity.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Integer> {
    List<ShoppingCart> findByUserId(Integer userId);

    void deleteByUserIdAndProductId(Integer userId, Integer productId);

    // Thêm method này để dùng cho updateQuantity
    Optional<ShoppingCart> findByUserIdAndProductId(Integer userId, Integer productId);
}
