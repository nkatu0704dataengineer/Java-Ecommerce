package com.ecomflow.admin.service;

import com.ecomflow.admin.dto.request.ProductCreateRequest;
import com.ecomflow.admin.dto.response.ProductResponse;
import com.ecomflow.admin.entity.Brand;
import com.ecomflow.admin.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface CatalogService {
    Page<ProductResponse> getProducts(String keyword, String categoryId, String brandId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    ProductResponse getProductById(String productId);
    ProductResponse createProduct(ProductCreateRequest request);
    List<Category> getAllCategories();
    List<Brand> getAllBrands();
}
