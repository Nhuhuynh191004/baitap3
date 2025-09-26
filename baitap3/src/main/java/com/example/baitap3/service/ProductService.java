package com.example.baitap3.service;

import com.example.baitap3.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Integer id);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    Product updateProductStatus(Integer id, Integer status);
    List<Product> getProductsByCategoryId(Integer categoryId);

    // tìm kiếm + phân trang + sort
    Page<Product> searchProducts(String name, Integer categoryId, Integer status, Pageable pageable);
}
