package com.group1.productcatalogsystem.mapper;

import com.group1.productcatalogsystem.dto.request.CategoryRequest;
import com.group1.productcatalogsystem.dto.response.CategoryResponse;
import com.group1.productcatalogsystem.entity.Category;
import com.group1.productcatalogsystem.util.StringValidationUtils;

public class CategoryMapper {

    private CategoryMapper() {
    }

    public static CategoryResponse toResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category toEntity(CategoryRequest request) {
        return Category.builder()
                .name(StringValidationUtils.requireNonBlankName(request.getName(), "Name"))
                .build();
    }

    public static void updateEntity(Category category, CategoryRequest request) {
        category.setName(StringValidationUtils.requireNonBlankName(request.getName(), "Name"));
    }
}
