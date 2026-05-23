package com.group1.productcatalogsystem.service;

import com.group1.productcatalogsystem.dto.request.CategoryRequest;
import com.group1.productcatalogsystem.dto.response.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse getCategoryById(Long id);

    List<CategoryResponse> getAllCategories();

    CategoryResponse updateCategory(Long id, CategoryRequest request);

    void deleteCategory(Long id);
}
