package com.example.baitap3.service;

import com.example.baitap3.entity.Product;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProducts();
    Optional<Product> getProductById(Integer id);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    Product updateProductStatus(Integer id, Integer status);
    List<Product> getProductsByCategoryId(Integer categoryId);
}
