package com.example.baitap3.service.impl;

import com.example.baitap3.entity.Product;
import com.example.baitap3.repository.ProductRepository;
import com.example.baitap3.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product updateProductStatus(Integer id, Integer status) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setStatus(status);
        return productRepository.save(product);
    }

    @Override
    public Page<Product> searchProducts(String name, Integer categoryId, Integer status, Pageable pageable) {
        return productRepository.search(name, categoryId, status, pageable);
    }

    @Override
    public List<Product> getProductsByCategoryId(Integer categoryId) {
        // Nếu bạn muốn trả về List thay vì Page, có thể lấy tất cả hoặc giới hạn
        return productRepository.findAll().stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(categoryId))
                .toList();
    }
}
