package com.group1.productcatalogsystem.service.impl;

import com.group1.productcatalogsystem.dto.request.CategoryRequest;
import com.group1.productcatalogsystem.dto.response.CategoryResponse;
import com.group1.productcatalogsystem.entity.Category;
import com.group1.productcatalogsystem.exception.BadRequestException;
import com.group1.productcatalogsystem.exception.ResourceNotFoundException;
import com.group1.productcatalogsystem.mapper.CategoryMapper;
import com.group1.productcatalogsystem.repository.CategoryRepository;
import com.group1.productcatalogsystem.repository.ProductRepository;
import com.group1.productcatalogsystem.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = CategoryMapper.toEntity(request);
        Category saved = categoryRepository.save(category);
        return CategoryMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        Category category = findCategoryOrThrow(id);
        return CategoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = findCategoryOrThrow(id);
        CategoryMapper.updateEntity(category, request);
        Category updated = categoryRepository.save(category);
        return CategoryMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) {
        Category category = findCategoryOrThrow(id);

        if (productRepository.existsByCategory_Id(id)) {
            throw new BadRequestException(
                    "Cannot delete category with id " + id + " because it has associated products");
        }

        try {
            categoryRepository.delete(category);
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException(
                    "Cannot delete category with id " + id + " because it is referenced by products");
        }
    }

    private Category findCategoryOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }
}
