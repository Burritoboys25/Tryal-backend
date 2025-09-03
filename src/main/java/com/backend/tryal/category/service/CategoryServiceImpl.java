package com.backend.tryal.category.service;

import com.backend.tryal.category.Category;
import com.backend.tryal.category.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

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
  public Category getCategoryById(UUID CategoryId) {
    Category category = categoryRepository.findById(CategoryId).orElse(null);
    if (category == null) {
      throw new EntityNotFoundException("Could not find category with id: " + CategoryId);
    }
    return category;
  }

  @Override
  public Category createCategory(Category category) {
    categoryRepository.save(category);

    return category;
  }

  @Override
  public Category updateCategoryById(UUID categoryId, Category category) {
    if (getCategoryById(categoryId) == null) {
      throw new EntityNotFoundException("Could not find category with id: " + categoryId);
    }
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

  @Override
  public void deleteCategoryById(UUID categoryId) {
    if (getCategoryById(categoryId) == null) {
      throw new EntityNotFoundException("Could not find category with id: " + categoryId);
    }
    categoryRepository.deleteById(categoryId);
  }
}
