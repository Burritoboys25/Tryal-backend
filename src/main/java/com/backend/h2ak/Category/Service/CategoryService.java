package com.backend.h2ak.Category.Service;

import com.backend.h2ak.Category.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    List<Category> getAllCategories();
    Category getCategoryById(UUID categoryId);
    Category createCategory(Category category);
    Category updateCategoryById(UUID categoryId, Category category);
    boolean deleteCategoryById(UUID categoryId);
}
