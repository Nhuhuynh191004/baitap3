package com.example.baitap3.service.impl;

import com.example.baitap3.entity.Category;
import com.example.baitap3.repository.CategoryRepository;
import com.example.baitap3.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategoryStatus(Integer id, Integer status) {
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setStatus(status);
        return categoryRepository.save(category);
    }
}
