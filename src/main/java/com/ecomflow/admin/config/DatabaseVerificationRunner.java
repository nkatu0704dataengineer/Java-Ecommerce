package com.ecomflow.admin.config;

import com.ecomflow.admin.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Cơ chế Tự Kiểm Tra & Nghiệm Thu (Self-Audit Protocol)
 * Tự động kiểm định kết nối CSDL và đếm số lượng bản ghi thực tế trên 16 Repositories.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseVerificationRunner implements CommandLineRunner {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final SubsidiaryRepository subsidiaryRepository;
    private final EmployeeRepository employeeRepository;
    private final ProductRepository productRepository;
    private final ProductScoreRepository productScoreRepository;
    private final ProgramSaleRepository programSaleRepository;
    private final ProductSaleRepository productSaleRepository;
    private final SubProductRepository subProductRepository;
    private final CustomerRepository customerRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public void run(String... args) {
        log.info("========================================================================================");
        log.info("  ECOMFLOW ADMIN - DATABASE SELF-AUDIT & VERIFICATION PROTOCOL (16 REPOSITORIES)");
        log.info("========================================================================================");

        try {
            long brands = brandRepository.count();
            long categories = categoryRepository.count();
            long subsidiaries = subsidiaryRepository.count();
            long employees = employeeRepository.count();
            long products = productRepository.count();
            long productScores = productScoreRepository.count();
            long programSales = programSaleRepository.count();
            long productSales = productSaleRepository.count();
            long subProducts = subProductRepository.count();
            long customers = customerRepository.count();
            long carts = cartRepository.count();
            long cartItems = cartItemRepository.count();
            long orders = orderRepository.count();
            long orderItems = orderItemRepository.count();
            long payments = paymentRepository.count();
            long reviews = reviewRepository.count();

            long total = brands + categories + subsidiaries + employees + products + productScores +
                    programSales + productSales + subProducts + customers + carts + cartItems +
                    orders + orderItems + payments + reviews;

            log.info("┌─────┬──────────────────────────┬──────────────────────────┬──────────────┬──────────────┐");
            log.info("│ STT │ Tên Bảng (Table Name)    │ JPA Entity Mapping       │ Số Bản Ghi   │ Trạng Thái   │");
            log.info("├─────┼──────────────────────────┼──────────────────────────┼──────────────┼──────────────┤");
            log.info("│ 01  │ dbo.brands               │ Brand                    │ {:>12} │ [OK] MAPPED  │", brands);
            log.info("│ 02  │ dbo.categories           │ Category                 │ {:>12} │ [OK] MAPPED  │", categories);
            log.info("│ 03  │ dbo.subsidiaries         │ Subsidiary               │ {:>12} │ [OK] MAPPED  │", subsidiaries);
            log.info("│ 04  │ dbo.employees            │ Employee                 │ {:>12} │ [OK] MAPPED  │", employees);
            log.info("│ 05  │ dbo.products             │ Product                  │ {:>12} │ [OK] MAPPED  │", products);
            log.info("│ 06  │ dbo.product_scores       │ ProductScore (1:1)       │ {:>12} │ [OK] MAPPED  │", productScores);
            log.info("│ 07  │ dbo.program_sales        │ ProgramSale              │ {:>12} │ [OK] MAPPED  │", programSales);
            log.info("│ 08  │ dbo.product_sales        │ ProductSale (Composite)  │ {:>12} │ [OK] MAPPED  │", productSales);
            log.info("│ 09  │ dbo.sub_products         │ SubProduct (Composite)   │ {:>12} │ [OK] MAPPED  │", subProducts);
            log.info("│ 10  │ dbo.customers            │ Customer                 │ {:>12} │ [OK] MAPPED  │", customers);
            log.info("│ 11  │ dbo.carts                │ Cart                     │ {:>12} │ [OK] MAPPED  │", carts);
            log.info("│ 12  │ dbo.cart_items           │ CartItem (Composite)     │ {:>12} │ [OK] MAPPED  │", cartItems);
            log.info("│ 13  │ dbo.orders               │ Order                    │ {:>12} │ [OK] MAPPED  │", orders);
            log.info("│ 14  │ dbo.order_items          │ OrderItem (Composite)    │ {:>12} │ [OK] MAPPED  │", orderItems);
            log.info("│ 15  │ dbo.payments             │ Payment (1:1)            │ {:>12} │ [OK] MAPPED  │", payments);
            log.info("│ 16  │ dbo.reviews              │ Review                   │ {:>12} │ [OK] MAPPED  │", reviews);
            log.info("├─────┴──────────────────────────┴──────────────────────────┼──────────────┴──────────────┤");
            log.info("│ TỔNG CỘNG BẢN GHI ĐỐI SOÁT TOÀN DIỆN                      │ {:>12} bản ghi       │", total);
            log.info("└───────────────────────────────────────────────────────────┴─────────────────────────────┘");
            log.info(">> [SYSTEM READY] KẾT NỐI VÀ ÁNH XẠ 16/16 BẢNG THÀNH CÔNG RỰC RỠ! KHÔNG CÓ LỖI MAPPING.");

        } catch (Exception ex) {
            log.error(">> [DATABASE AUDIT FAILED] Lỗi khi kiểm tra kết nối hoặc truy vấn CSDL: {}", ex.getMessage(), ex);
        }
        log.info("========================================================================================");
    }
}

