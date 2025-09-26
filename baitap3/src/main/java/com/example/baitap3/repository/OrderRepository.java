package com.example.baitap3.repository;

import com.example.baitap3.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    // Lấy danh sách order theo userId
    List<Order> findByUserId(Integer userId);
}
