package com.ecomflow.admin.controller;

import com.ecomflow.admin.common.ApiResponse;
import com.ecomflow.admin.dto.request.ProductCreateRequest;
import com.ecomflow.admin.dto.response.ProductResponse;
import com.ecomflow.admin.entity.Brand;
import com.ecomflow.admin.entity.Category;
import com.ecomflow.admin.service.CatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * Controller quản lý Sản phẩm, Ngành hàng và Thương hiệu
 */
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) String brandId,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @PageableDefault(size = 20, sort = "productId", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<ProductResponse> page = catalogService.getProducts(keyword, categoryId, brandId, minPrice, maxPrice, pageable);
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách sản phẩm thành công", page));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable String id) {
        ProductResponse product = catalogService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.ok(product));
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        ProductResponse created = catalogService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Tạo sản phẩm mới thành công", created));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<Category>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách ngành hàng thành công", catalogService.getAllCategories()));
    }

    @GetMapping("/brands")
    public ResponseEntity<ApiResponse<List<Brand>>> getBrands() {
        return ResponseEntity.ok(ApiResponse.ok("Lấy danh sách thương hiệu thành công", catalogService.getAllBrands()));
    }
}

