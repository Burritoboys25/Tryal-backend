package com.backend.tryal.category.service;

import com.backend.tryal.category.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(UUID categoryId);
    Category createCategory(Category category);
    Category updateCategoryById(UUID categoryId, Category category);
    void deleteCategoryById(UUID categoryId);
}
