package com.example.baitap3.service;

import com.example.baitap3.entity.Category;
import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategories();
    Optional<Category> getCategoryById(Integer id);
    Category createCategory(Category category);
    Category updateCategory(Category category);
    Category updateCategoryStatus(Integer id, Integer status);
}
