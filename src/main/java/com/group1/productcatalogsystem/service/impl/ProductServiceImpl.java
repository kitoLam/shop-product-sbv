package com.group1.productcatalogsystem.service.impl;

import com.group1.productcatalogsystem.dto.request.ProductRequest;
import com.group1.productcatalogsystem.dto.response.ProductResponse;
import com.group1.productcatalogsystem.entity.Category;
import com.group1.productcatalogsystem.entity.Product;
import com.group1.productcatalogsystem.exception.BadRequestException;
import com.group1.productcatalogsystem.exception.ResourceNotFoundException;
import com.group1.productcatalogsystem.mapper.ProductMapper;
import com.group1.productcatalogsystem.repository.CategoryRepository;
import com.group1.productcatalogsystem.repository.ProductRepository;
import com.group1.productcatalogsystem.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = findCategoryOrThrow(request.getCategoryId());
        validateProductRequest(request);

        Product product = ProductMapper.toEntity(request, category);
        Product saved = productRepository.save(product);
        return ProductMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = findProductOrThrow(id);
        return ProductMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProductsByName(String name, Pageable pageable) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("Search name must not be blank");
        }
        return productRepository.findByNameContainingIgnoreCase(name.trim(), pageable)
                .map(ProductMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        findCategoryOrThrow(categoryId);
        return productRepository.findByCategory_Id(categoryId)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProductOrThrow(id);
        Category category = findCategoryOrThrow(request.getCategoryId());
        validateProductRequest(request);

        ProductMapper.updateEntity(product, request, category);
        Product updated = productRepository.save(product);
        return ProductMapper.toResponse(updated);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = findProductOrThrow(id);
        productRepository.delete(product);
    }

    private Product findProductOrThrow(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    private Category findCategoryOrThrow(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + categoryId));
    }

    private void validateProductRequest(ProductRequest request) {
        if (request.getPrice() == null || request.getPrice().signum() <= 0) {
            throw new BadRequestException("Price must be greater than 0");
        }
        Integer stockQuantity = request.getStockQuantity();
        if (stockQuantity != null && stockQuantity < 0) {
            throw new BadRequestException("Stock quantity must be 0 or greater");
        }
    }
}
