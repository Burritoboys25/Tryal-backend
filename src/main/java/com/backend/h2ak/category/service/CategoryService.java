package com.backend.h2ak.category.service;

import com.backend.h2ak.category.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(UUID categoryId);
    Category createCategory(Category category);
    Category updateCategoryById(UUID categoryId, Category category);
    boolean deleteCategoryById(UUID categoryId);
}
