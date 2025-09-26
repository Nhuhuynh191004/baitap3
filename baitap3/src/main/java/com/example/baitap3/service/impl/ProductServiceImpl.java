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
        Product product = productRepository.findById(id).orElseThrow();
        product.setStatus(status);
        return productRepository.save(product);
    }

        @Override
        public List<Product> getProductsByCategoryId(Integer categoryId) {
            return productRepository.findByCategoryId(categoryId);
        }
@Override
public Page<Product> searchProducts(String name, Integer categoryId, Integer status, Pageable pageable) {
    return productRepository.searchProducts(name, categoryId, status, pageable);
}
}
