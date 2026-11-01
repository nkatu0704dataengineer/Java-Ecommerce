package com.ecomflow.admin.service.impl;

import com.ecomflow.admin.common.exception.BadRequestException;
import com.ecomflow.admin.common.exception.ResourceNotFoundException;
import com.ecomflow.admin.dto.request.ProductCreateRequest;
import com.ecomflow.admin.dto.response.ProductResponse;
import com.ecomflow.admin.entity.*;
import com.ecomflow.admin.repository.BrandRepository;
import com.ecomflow.admin.repository.CategoryRepository;
import com.ecomflow.admin.repository.ProductRepository;
import com.ecomflow.admin.repository.ProductScoreRepository;
import com.ecomflow.admin.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CatalogServiceImpl implements CatalogService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductScoreRepository productScoreRepository;

    @Override
    public Page<ProductResponse> getProducts(String keyword, String categoryId, String brandId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        Page<Product> products = productRepository.searchProducts(keyword, categoryId, brandId, minPrice, maxPrice, pageable);
        return products.map(this::mapToResponse);
    }

    @Override
    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có mã: " + productId));
        return mapToResponse(product);
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsById(request.getProductId())) {
            throw new BadRequestException("Mã sản phẩm [" + request.getProductId() + "] đã tồn tại trên hệ thống!");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành hàng có mã: " + request.getCategoryId()));

        Brand brand = brandRepository.findById(request.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy thương hiệu có mã: " + request.getBrandId()));

        Product product = Product.builder()
                .productId(request.getProductId())
                .productName(request.getProductName())
                .category(category)
                .brand(brand)
                .productListprice(request.getProductListprice())
                .productWeightG(request.getProductWeightG())
                .productDescription(request.getProductDescription())
                .productVersion(request.getProductVersion() != null ? request.getProductVersion() : "Standard")
                .productStatus("ACTIVE")
                .productPhotosLink(request.getProductPhotosLink())
                .build();

        Product savedProduct = productRepository.save(product);

        // Khởi tạo điểm đánh giá ban đầu 1:1
        ProductScore score = ProductScore.builder()
                .productId(savedProduct.getProductId())
                .product(savedProduct)
                .reviewCount(0)
                .avgRating(BigDecimal.valueOf(5.0))
                .build();
        productScoreRepository.save(score);
        savedProduct.setScore(score);

        return mapToResponse(savedProduct);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productVersion(product.getProductVersion())
                .categoryId(product.getCategory() != null ? product.getCategory().getCateId() : null)
                .categoryName(product.getCategory() != null ? product.getCategory().getCateName() : null)
                .brandId(product.getBrand() != null ? product.getBrand().getBrandId() : null)
                .brandName(product.getBrand() != null ? product.getBrand().getBrandName() : null)
                .productListprice(product.getProductListprice())
                .avgRating(product.getScore() != null ? product.getScore().getAvgRating() : product.getProductRating())
                .reviewCount(product.getScore() != null ? product.getScore().getReviewCount() : product.getReviewCount())
                .productStatus(product.getProductStatus())
                .productWeightG(product.getProductWeightG())
                .productDescription(product.getProductDescription())
                .productPhotosLink(product.getProductPhotosLink())
                .build();
    }
}
