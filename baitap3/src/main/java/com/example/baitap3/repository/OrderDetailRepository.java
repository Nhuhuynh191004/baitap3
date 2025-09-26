package com.example.baitap3.repository;

import com.example.baitap3.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    // Lấy danh sách order detail theo orderId
    List<OrderDetail> findByOrderId(Integer orderId);
}
