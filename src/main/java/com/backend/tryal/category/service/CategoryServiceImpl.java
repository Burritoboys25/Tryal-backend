package com.backend.tryal.category.service;

import com.backend.tryal.category.Category;
import com.backend.tryal.category.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long CategoryId) {
        return categoryRepository.findById(CategoryId).orElse(null);
    }

    @Override
    public Category createCategory(Category category) {
        categoryRepository.save(category);

        return category;
    }

    @Override
    public Category updateCategoryById(Long categoryId, Category category) {
        if (getCategoryById(categoryId) != null) {
            Category updatedCategory = getCategoryById(categoryId);

            if (category.getName() != null) {
                updatedCategory.setName(category.getName());
            }

            if (category.getDescription() != null) {
                updatedCategory.setDescription(category.getDescription());
            }

            categoryRepository.save(updatedCategory);
            return updatedCategory;
        }
        return null;
    }

    @Override
    public boolean deleteCategoryById(Long categoryId) {
        if (getCategoryById(categoryId) != null) {
            categoryRepository.deleteById(categoryId);
            return true;
        }

        return false;
    }
}
