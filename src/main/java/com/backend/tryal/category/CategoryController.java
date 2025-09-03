package com.backend.tryal.category;

import com.backend.tryal.category.service.CategoryService;
import com.backend.tryal.shared.response.ApiResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

  private final CategoryService categoryService;

  public CategoryController(CategoryService categoryService) {
    this.categoryService = categoryService;
  }

  // get all Categories
  @GetMapping()
  public List<Category> getAllCategories() {
    return categoryService.getAllCategories();
  }

  // get Category by ID
  @GetMapping("/{categoryId}")
  public Category getCategoryById(@PathVariable UUID categoryId) {
    return categoryService.getCategoryById(categoryId);
  }

  // Create Category
  @PostMapping()
  public Category createCategory(@RequestBody Category category) {
    return categoryService.createCategory(category);
  }

  // Patch Category
  @PatchMapping("/{categoryId}")
  public Category updateCategoryById(@RequestBody Category category,
      @PathVariable UUID categoryId) {
    return categoryService.updateCategoryById(categoryId, category);
  }

  // Delete Category
  @DeleteMapping("/{categoryId}")
  public ApiResponse<String> deleteCategoryById(@PathVariable UUID categoryId) {
    categoryService.deleteCategoryById(categoryId);
    return new ApiResponse<>("Category deleted successfully.");
  }
}
