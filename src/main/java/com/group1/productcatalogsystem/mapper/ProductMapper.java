package com.group1.productcatalogsystem.mapper;

import com.group1.productcatalogsystem.dto.request.ProductRequest;
import com.group1.productcatalogsystem.dto.response.ProductResponse;
import com.group1.productcatalogsystem.entity.Category;
import com.group1.productcatalogsystem.entity.Product;
import com.group1.productcatalogsystem.util.StringValidationUtils;

public class ProductMapper {

    private ProductMapper() {
    }

    public static ProductResponse toResponse(Product product) {
        ProductResponse.ProductResponseBuilder builder = ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity());

        if (product.getCategory() != null) {
            builder.categoryId(product.getCategory().getId())
                    .categoryName(product.getCategory().getName());
        }

        return builder.build();
    }

    public static Product toEntity(ProductRequest request, Category category) {
        return Product.builder()
                .name(StringValidationUtils.requireNonBlankName(request.getName(), "Name"))
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(resolveStockQuantity(request.getStockQuantity()))
                .category(category)
                .build();
    }

    public static void updateEntity(Product product, ProductRequest request, Category category) {
        product.setName(StringValidationUtils.requireNonBlankName(request.getName(), "Name"));
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(resolveStockQuantity(request.getStockQuantity()));
        product.setCategory(category);
    }

    private static int resolveStockQuantity(Integer stockQuantity) {
        return stockQuantity != null ? stockQuantity : 0;
    }
}
