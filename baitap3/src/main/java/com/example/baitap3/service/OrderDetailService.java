package com.example.baitap3.service;

import com.example.baitap3.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> getOrderDetailsByOrderId(Integer orderId);
}
