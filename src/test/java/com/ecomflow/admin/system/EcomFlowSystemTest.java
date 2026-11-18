package com.ecomflow.admin.system;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.*;
import com.microsoft.playwright.options.RequestOptions;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ==============================================================================
 * ECOMFLOW ADMIN - BỘ KIỂM THỬ HỆ THỐNG & E2E AUTOMATION TEST SUITE
 * ==============================================================================
 * Công nghệ: Playwright for Java 1.48.0 + JUnit 5
 * Phạm vi: 9 Test Cases xuyên suốt 3 nhóm nghiệp vụ
 * Tính chất: Idempotent (Chạy lại nhiều lần vẫn Pass 100%, tự động dọn dẹp dữ liệu)
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EcomFlowSystemTest {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String JDBC_URL = "jdbc:sqlserver://localhost:1433;databaseName=ecomflow_db;encrypt=true;trustServerCertificate=true";
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "Ecomflow@2025";

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private APIRequestContext request;
    private ObjectMapper objectMapper;

    @BeforeAll
    void setupSuite() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        context = browser.newContext(new Browser.NewContextOptions().setViewportSize(1440, 900));
        page = context.newPage();
        request = playwright.request().newContext(new APIRequest.NewContextOptions().setBaseURL(BASE_URL));
        objectMapper = new ObjectMapper();
    }

    @AfterAll
    void teardownSuite() {
        // Dọn dẹp dữ liệu kiểm thử (Idempotent cleanup)
        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("DELETE FROM product_scores WHERE product_id LIKE 'PR_TEST_%'");
            stmt.executeUpdate("DELETE FROM products WHERE product_id LIKE 'PR_TEST_%'");
        } catch (Exception e) {
            System.err.println("Database cleanup warning: " + e.getMessage());
        }

        if (request != null) {
            request.dispose();
        }
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    // ==========================================================================
    // NHÓM 1: KIỂM THỬ KẾT NỐI VÀ TÍNH TOÀN VẸN CƠ SỞ DỮ LIỆU (3 TEST CASES)
    // ==========================================================================

    @Test
    @Order(1)
    @DisplayName("TC_DB_01: Kiểm tra kết nối Live DB & đồng bộ 16 bảng")
    void tc_db_01_verifyLiveDbConnectionAndSync() {
        // 1. Thăm dò API qua APIRequestContext
        APIResponse response = request.get("/api/v1/dashboard/kpis");
        assertEquals(200, response.status(), "HTTP status của /api/v1/dashboard/kpis phải là 200 OK");

        try {
            JsonNode json = objectMapper.readTree(response.body());
            assertTrue(json.get("success").asBoolean(), "Thuộc tính success trong ApiResponse phải là true");
            assertNotNull(json.get("data"), "Dữ liệu KPI data không được null");
            assertTrue(json.get("data").has("totalRevenue"), "Dữ liệu KPI phải chứa trường totalRevenue");
        } catch (Exception e) {
            fail("Lỗi giải mã JSON từ API: " + e.getMessage());
        }

        // 2. Mở giao diện bằng Page và đối soát Topbar Badge
        page.navigate(BASE_URL);
        page.waitForSelector("#dbStatusLabel:has-text('Live DB: SQL Server 2025 (ecomflow_db)')",
                new Page.WaitForSelectorOptions().setTimeout(15000));

        String labelText = page.textContent("#dbStatusLabel");
        assertTrue(labelText.contains("Live DB: SQL Server 2025 (ecomflow_db)"),
                "Thanh Header phải hiển thị badge Live DB kết nối SQL Server 2025");
        assertTrue(page.locator("#dbStatusDot").isVisible(), "Biểu tượng trạng thái kết nối phải hiển thị");
    }

    @Test
    @Order(2)
    @DisplayName("TC_DB_02: Kiểm tra ràng buộc chống trùng khóa chính (Duplicate PK)")
    void tc_db_02_verifyDuplicatePrimaryKeyProtection() {
        // Mã PR001 đã tồn tại trong dbo.products
        Map<String, Object> duplicatePayload = new HashMap<>();
        duplicatePayload.put("productId", "PR001");
        duplicatePayload.put("productName", "Test Duplicate Product Protection");
        duplicatePayload.put("categoryId", "CAT01");
        duplicatePayload.put("brandId", "BRAND01");
        duplicatePayload.put("productListprice", 1000000);

        APIResponse response = request.post("/api/v1/products",
                RequestOptions.create().setData(duplicatePayload));

        assertEquals(400, response.status(), "API phải trả về mã lỗi HTTP 400 Bad Request khi trùng khóa chính");

        try {
            JsonNode json = objectMapper.readTree(response.body());
            assertFalse(json.get("success").asBoolean(), "success phải là false khi tạo trùng mã");
            String message = json.get("message").asText().toLowerCase();
            assertTrue(message.contains("đã tồn tại") || message.contains("duplicate") || message.contains("pr001"),
                    "Thông điệp cảnh báo phải chỉ rõ mã sản phẩm đã tồn tại");
        } catch (Exception e) {
            fail("Lỗi giải mã JSON phản hồi lỗi: " + e.getMessage());
        }
    }

    @Test
    @Order(3)
    @DisplayName("TC_DB_03: Kiểm tra toàn vẹn tham chiếu 1:1 & Khóa ngoại (@MapsId)")
    void tc_db_03_verifyOneToOneReferentialIntegrityAndForeignKeys() {
        String testProductId = "PR_TEST_" + (System.currentTimeMillis() % 1000000);

        Map<String, Object> payload = new HashMap<>();
        payload.put("productId", testProductId);
        payload.put("productName", "Laptop Dell Precision Test Automation");
        payload.put("categoryId", "CAT01");
        payload.put("brandId", "BRAND01");
        payload.put("productListprice", 25000000);
        payload.put("productWeightG", 2300.0);
        payload.put("productDescription", "Kiểm thử tự động toàn vẹn quan hệ 1:1 @MapsId");

        // 1. Tạo sản phẩm mới
        APIResponse postRes = request.post("/api/v1/products",
                RequestOptions.create().setData(payload));
        assertTrue(postRes.status() == 201 || postRes.status() == 200,
                "Tạo sản phẩm thành công phải trả về HTTP 201 Created hoặc 200 OK");

        try {
            JsonNode postJson = objectMapper.readTree(postRes.body());
            assertTrue(postJson.get("success").asBoolean(), "success phải là true khi tạo mới");

            // 2. Tra cứu lại sản phẩm vừa tạo
            APIResponse getRes = request.get("/api/v1/products/" + testProductId);
            assertEquals(200, getRes.status(), "GET /api/v1/products/{id} phải trả về HTTP 200 OK");

            JsonNode getJson = objectMapper.readTree(getRes.body());
            JsonNode data = getJson.get("data");
            assertEquals(testProductId, data.get("productId").asText(), "Mã sản phẩm trả về phải khớp mã tạo");
            assertEquals("CAT01", data.get("categoryId").asText(), "Mã ngành hàng phải khớp");
            assertEquals("BRAND01", data.get("brandId").asText(), "Mã thương hiệu phải khớp");

            // 3. Khóa ngoại và Quan hệ 1:1 (@MapsId) điểm đánh giá tự động khởi tạo 5.0
            assertNotNull(data.get("avgRating"), "Trường điểm số avgRating từ product_scores không được null");
            assertEquals(5.0, data.get("avgRating").asDouble(), 0.01,
                    "Điểm số khởi tạo ban đầu cho sản phẩm mới phải là 5.0 sao");
        } catch (Exception e) {
            fail("Lỗi giải mã JSON sản phẩm: " + e.getMessage());
        } finally {
            // Dọn dẹp ngay sau khi kiểm tra xong để bảo đảm idempotency
            try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASS);
                 Statement stmt = conn.createStatement()) {
                stmt.executeUpdate("DELETE FROM product_scores WHERE product_id = '" + testProductId + "'");
                stmt.executeUpdate("DELETE FROM products WHERE product_id = '" + testProductId + "'");
            } catch (Exception ignored) {
            }
        }
    }

    // ==========================================================================
    // NHÓM 2: KIỂM THỬ CHỨC NĂNG NGHIỆP VỤ (3 TEST CASES)
    // ==========================================================================

    @Test
    @Order(4)
    @DisplayName("TC_BIZ_01: Kiểm tra điều phối tồn kho & chặn xuất âm kho")
    void tc_biz_01_verifyInventoryAdjustmentAndNegativeStockPrevention() {
        String subsidiaryId = "SUB_01";
        String productId = "PR001";

        // 1. Lấy tồn kho ban đầu
        APIResponse invRes = request.get("/api/v1/inventory?subsidiaryId=" + subsidiaryId + "&keyword=" + productId);
        assertEquals(200, invRes.status(), "Truy vấn tồn kho phải trả về 200 OK");

        int initialQty = 0;
        try {
            JsonNode invJson = objectMapper.readTree(invRes.body());
            JsonNode content = invJson.get("data").get("content");
            assertTrue(content.isArray() && content.size() > 0, "Phải tìm thấy dòng tồn kho của PR001 tại SUB_01");
            initialQty = content.get(0).get("quantity").asInt();
        } catch (Exception e) {
            fail("Lỗi đọc số lượng tồn kho ban đầu: " + e.getMessage());
        }

        // 2. Nhập kho: adjustType = IN, changeQuantity = 50
        Map<String, Object> adjustIn = new HashMap<>();
        adjustIn.put("subsidiaryId", subsidiaryId);
        adjustIn.put("productId", productId);
        adjustIn.put("adjustType", "IN");
        adjustIn.put("changeQuantity", 50);

        APIResponse inRes = request.post("/api/v1/inventory/adjust",
                RequestOptions.create().setData(adjustIn));
        assertEquals(200, inRes.status(), "Nhập kho hợp lệ phải trả về 200 OK");

        try {
            JsonNode inJson = objectMapper.readTree(inRes.body());
            int afterInQty = inJson.get("data").get("quantity").asInt();
            assertEquals(initialQty + 50, afterInQty, "Số lượng sau nhập phải tăng chính xác +50");
        } catch (Exception e) {
            fail("Lỗi đọc số lượng tồn kho sau nhập: " + e.getMessage());
        }

        // 3. Xuất quá số lượng tồn (xuất âm): adjustType = OUT, changeQuantity = 999999 -> Chặn HTTP 400
        Map<String, Object> adjustOver = new HashMap<>();
        adjustOver.put("subsidiaryId", subsidiaryId);
        adjustOver.put("productId", productId);
        adjustOver.put("adjustType", "OUT");
        adjustOver.put("changeQuantity", 999999);

        APIResponse overRes = request.post("/api/v1/inventory/adjust",
                RequestOptions.create().setData(adjustOver));
        assertEquals(400, overRes.status(), "Xuất âm kho bắt buộc phải bị chặn với mã HTTP 400 Bad Request");

        try {
            JsonNode overJson = objectMapper.readTree(overRes.body());
            assertFalse(overJson.get("success").asBoolean(), "success phải là false khi xuất âm kho");
            String message = overJson.get("message").asText().toLowerCase();
            assertTrue(message.contains("không đủ") || message.contains("tồn kho"),
                    "Thông điệp phải nêu rõ số lượng tồn không đủ để xuất");
        } catch (Exception e) {
            fail("Lỗi giải mã phản hồi chặn xuất âm kho: " + e.getMessage());
        }

        // 4. Khôi phục lại tồn kho ban đầu: adjustType = OUT, changeQuantity = 50
        Map<String, Object> adjustRestore = new HashMap<>();
        adjustRestore.put("subsidiaryId", subsidiaryId);
        adjustRestore.put("productId", productId);
        adjustRestore.put("adjustType", "OUT");
        adjustRestore.put("changeQuantity", 50);

        APIResponse restoreRes = request.post("/api/v1/inventory/adjust",
                RequestOptions.create().setData(adjustRestore));
        assertEquals(200, restoreRes.status(), "Hoàn trả tồn kho phải trả về 200 OK");

        try {
            JsonNode restoreJson = objectMapper.readTree(restoreRes.body());
            int finalQty = restoreJson.get("data").get("quantity").asInt();
            assertEquals(initialQty, finalQty, "Tồn kho sau khi hoàn trả phải trở về đúng số lượng ban đầu");
        } catch (Exception e) {
            fail("Lỗi đọc tồn kho hoàn trả: " + e.getMessage());
        }
    }

    @Test
    @Order(5)
    @DisplayName("TC_BIZ_02: Kiểm tra cơ chế khóa mềm tài khoản khách hàng (Soft Active Toggle)")
    void tc_biz_02_verifyCustomerSoftActiveToggle() {
        String customerId = "ACC0001";

        // 1. Lấy trạng thái isActive ban đầu
        APIResponse getCus = request.get("/api/v1/customers?keyword=" + customerId);
        assertEquals(200, getCus.status(), "Tra cứu khách hàng phải trả về 200 OK");

        boolean initialActive = true;
        String fullName = "";
        try {
            JsonNode cusJson = objectMapper.readTree(getCus.body());
            JsonNode content = cusJson.get("data").get("content");
            assertTrue(content.isArray() && content.size() > 0, "Phải tìm thấy khách hàng " + customerId);
            initialActive = content.get(0).get("isActive").asBoolean();
            fullName = content.get(0).get("fullName").asText();
        } catch (Exception e) {
            fail("Lỗi đọc trạng thái khách hàng ban đầu: " + e.getMessage());
        }

        // 2. Gọi PATCH toggle-active lần 1: Đảo trạng thái
        APIResponse toggleRes1 = request.patch("/api/v1/customers/" + customerId + "/toggle-active");
        assertEquals(200, toggleRes1.status(), "Toggle trạng thái phải trả về 200 OK");

        try {
            JsonNode toggleJson1 = objectMapper.readTree(toggleRes1.body());
            JsonNode data1 = toggleJson1.get("data");
            assertEquals(!initialActive, data1.get("isActive").asBoolean(),
                    "Trạng thái isActive sau lần 1 phải bị đảo ngược (true <-> false)");
            assertEquals(fullName, data1.get("fullName").asText(),
                    "Thông tin hồ sơ khách hàng vẫn phải được giữ nguyên toàn vẹn");
        } catch (Exception e) {
            fail("Lỗi đọc phản hồi toggle lần 1: " + e.getMessage());
        }

        // 3. Gọi PATCH toggle-active lần 2: Khôi phục trạng thái ban đầu
        APIResponse toggleRes2 = request.patch("/api/v1/customers/" + customerId + "/toggle-active");
        assertEquals(200, toggleRes2.status(), "Khôi phục trạng thái phải trả về 200 OK");

        try {
            JsonNode toggleJson2 = objectMapper.readTree(toggleRes2.body());
            JsonNode data2 = toggleJson2.get("data");
            assertEquals(initialActive, data2.get("isActive").asBoolean(),
                    "Trạng thái isActive sau lần 2 phải khôi phục về đúng giá trị gốc ban đầu");
        } catch (Exception e) {
            fail("Lỗi đọc phản hồi toggle lần 2: " + e.getMessage());
        }
    }

    @Test
    @Order(6)
    @DisplayName("TC_BIZ_03: Kiểm tra nghiệp vụ phản hồi đánh giá CSKH (NLP Review Reply)")
    void tc_biz_03_verifyCustomerReviewReplyWorkflow() {
        // 1. Tìm động 1 reviewId chưa có phản hồi (hasReply = false)
        APIResponse revRes = request.get("/api/v1/reviews?hasReply=false&page=0&size=5");
        assertEquals(200, revRes.status(), "Tra cứu đánh giá phải trả về 200 OK");

        String targetReviewId = null;
        try {
            JsonNode revJson = objectMapper.readTree(revRes.body());
            JsonNode content = revJson.get("data").get("content");
            if (content.isArray() && content.size() > 0) {
                targetReviewId = content.get(0).get("reviewId").asText();
            } else {
                // Nếu tất cả đã có phản hồi thì lấy 1 review bất kỳ
                APIResponse allRes = request.get("/api/v1/reviews?page=0&size=5");
                JsonNode allJson = objectMapper.readTree(allRes.body());
                targetReviewId = allJson.get("data").get("content").get(0).get("reviewId").asText();
            }
        } catch (Exception e) {
            fail("Lỗi lấy danh sách đánh giá: " + e.getMessage());
        }

        assertNotNull(targetReviewId, "Phải tìm được 1 reviewId hợp lệ để kiểm thử");

        // 2. Gửi phản hồi CSKH: POST /api/v1/reviews/{id}/reply
        Map<String, Object> replyPayload = new HashMap<>();
        replyPayload.put("replyMessage", "EcomFlow CSKH cảm ơn quý khách đã tin dùng sản phẩm!");

        APIResponse replyRes = request.post("/api/v1/reviews/" + targetReviewId + "/reply",
                RequestOptions.create().setData(replyPayload));
        assertEquals(200, replyRes.status(), "Gửi phản hồi CSKH phải trả về HTTP 200 OK");

        try {
            JsonNode replyJson = objectMapper.readTree(replyRes.body());
            assertTrue(replyJson.get("success").asBoolean(), "success phải là true");
            JsonNode data = replyJson.get("data");
            assertTrue(data.get("hasReply").asBoolean(), "Cờ hasReply phải chuyển sang true");
            assertNotNull(data.get("repliedAt"), "Trường repliedAt phải được ghi nhận");
            assertFalse(data.get("repliedAt").asText().isEmpty(), "Thời gian repliedAt không được rỗng");
        } catch (Exception e) {
            fail("Lỗi phân tích phản hồi CSKH: " + e.getMessage());
        }
    }

    // ==========================================================================
    // NHÓM 3: KIỂM THỬ TÍCH HỢP HỆ THỐNG (3 TEST CASES)
    // ==========================================================================

    @Test
    @Order(7)
    @DisplayName("TC_INT_01: Tích hợp luồng đối soát KPI & Biểu đồ trên Executive Dashboard")
    void tc_int_01_verifyExecutiveDashboardKpisAndCharts() {
        // 1. Lấy dữ liệu KPI chuẩn từ backend
        APIResponse kpiRes = request.get("/api/v1/dashboard/kpis");
        assertEquals(200, kpiRes.status(), "API KPI phải trả về 200 OK");

        int expectedOrders = 0;
        int expectedActiveCustomers = 0;
        int expectedLowStock = 0;

        try {
            JsonNode kpiJson = objectMapper.readTree(kpiRes.body());
            JsonNode data = kpiJson.get("data");
            expectedOrders = data.get("totalOrders").asInt();
            expectedActiveCustomers = data.get("activeCustomers").asInt();
            expectedLowStock = data.get("lowStockCount").asInt();
        } catch (Exception e) {
            fail("Lỗi đọc dữ liệu KPI: " + e.getMessage());
        }

        // 2. Mở Dashboard và đối soát các thẻ KPI trên DOM
        page.navigate(BASE_URL);
        page.waitForSelector("#dbStatusLabel:has-text('Live DB: SQL Server 2025 (ecomflow_db)')",
                new Page.WaitForSelectorOptions().setTimeout(15000));
        page.waitForFunction("expected => document.getElementById('kpiTotalOrders').textContent.replace(/[.,]/g, '').trim().includes(expected)",
                String.valueOf(expectedOrders));

        String domOrders = page.textContent("#kpiTotalOrders").replace(".", "").replace(",", "").trim();
        assertTrue(domOrders.contains(String.valueOf(expectedOrders)),
                "Thẻ KPI Total Orders trên DOM (" + domOrders + ") phải khớp số liệu API (" + expectedOrders + ")");

        String domCust = page.textContent("#kpiActiveCustomers").replace(".", "").replace(",", "").trim();
        assertTrue(domCust.contains(String.valueOf(expectedActiveCustomers)),
                "Thẻ KPI Active Customers trên DOM (" + domCust + ") phải chứa số liệu API (" + expectedActiveCustomers + ")");

        String domStock = page.textContent("#kpiLowStock").replace(".", "").replace(",", "").trim();
        assertTrue(domStock.contains(String.valueOf(expectedLowStock)),
                "Thẻ KPI Low Stock trên DOM (" + domStock + ") phải khớp số liệu API (" + expectedLowStock + ")");

        // 3. Kiểm tra các biểu đồ Chart.js (Canvas elements) được hiển thị
        assertTrue(page.locator("#chartRevenueTrend").isVisible(), "Biểu đồ đường Doanh thu phải hiển thị");
        assertTrue(page.locator("#chartSentiment").isVisible(), "Biểu đồ sắc thái cảm xúc NLP phải hiển thị");
        assertTrue(page.locator("#chartTopBranches").isVisible(), "Biểu đồ Top chi nhánh phải hiển thị");
    }

    @Test
    @Order(8)
    @DisplayName("TC_INT_02: Tích hợp luồng chi tiết đơn hàng (JOIN FETCH) & cập nhật trạng thái")
    void tc_int_02_verifyOrderDetailJoinFetchAndStatusTransition() {
        // 1. Lấy 1 đơn hàng thực tế
        APIResponse ordersRes = request.get("/api/v1/orders?page=0&size=5");
        assertEquals(200, ordersRes.status(), "API đơn hàng phải trả về 200 OK");

        String orderId = null;
        String initialStatus = null;
        try {
            JsonNode ordersJson = objectMapper.readTree(ordersRes.body());
            JsonNode firstOrder = ordersJson.get("data").get("content").get(0);
            orderId = firstOrder.get("orderId").asText();
            initialStatus = firstOrder.get("orderStatus").asText();
        } catch (Exception e) {
            fail("Lỗi lấy đơn hàng mẫu: " + e.getMessage());
        }

        assertNotNull(orderId, "Mã đơn hàng mẫu không được null");

        // 2. Tra cứu chi tiết đơn hàng qua API (kiểm tra Eager Fetching JOIN FETCH)
        APIResponse detailRes = request.get("/api/v1/orders/" + orderId);
        assertEquals(200, detailRes.status(), "API chi tiết đơn hàng phải trả về 200 OK");

        try {
            JsonNode detailJson = objectMapper.readTree(detailRes.body());
            JsonNode data = detailJson.get("data");
            assertNotNull(data.get("customerId"), "Thông tin khách hàng phải được tải đầy đủ");
            assertTrue(data.get("items").isArray() && data.get("items").size() > 0,
                    "Danh sách order_items phải có ít nhất 1 mặt hàng");
            assertNotNull(data.get("payment"), "Quan hệ 1:1 với payments phải được tải");
            assertNotNull(data.get("payment").get("paymentId"), "Mã paymentId đối soát không được null");
        } catch (Exception e) {
            fail("Lỗi giải mã chi tiết đơn hàng: " + e.getMessage());
        }

        // 3. Mở chi tiết đơn hàng trên giao diện UI (Offcanvas Drawer)
        page.navigate(BASE_URL);
        page.waitForSelector("#mainSidebar");
        page.evaluate("(id) => window.openOrderDetail(id)", orderId);
        page.waitForSelector("#orderDetailOffcanvas.show", new Page.WaitForSelectorOptions().setTimeout(10000));

        assertEquals(orderId, page.textContent("#offcanvasOrderId").trim(),
                "Offcanvas Drawer phải hiển thị đúng mã đơn hàng");
        assertFalse(page.textContent("#offcanvasCustomer").trim().isEmpty(),
                "Tên khách hàng trên Offcanvas không được rỗng");
        assertTrue(page.locator("#offcanvasOrderItemsBody tr").count() >= 1,
                "Bảng mặt hàng trong Offcanvas phải có ít nhất 1 dòng");
        assertTrue(page.textContent("#offcanvasPaymentId").trim().contains("PAY_")
                        || !page.textContent("#offcanvasPaymentId").trim().isEmpty(),
                "Thông tin đối soát thanh toán phải hiển thị trên UI");

        // 4. Cập nhật trạng thái đơn hàng sang DELIVERED
        APIResponse updateRes = request.patch("/api/v1/orders/" + orderId + "/status?newStatus=DELIVERED");
        assertEquals(200, updateRes.status(), "Cập nhật trạng thái đơn hàng phải trả về 200 OK");

        try {
            JsonNode updateJson = objectMapper.readTree(updateRes.body());
            assertEquals("DELIVERED", updateJson.get("data").get("orderStatus").asText(),
                    "Trạng thái đơn hàng sau cập nhật phải là DELIVERED");
        } catch (Exception e) {
            fail("Lỗi giải mã trạng thái đơn hàng cập nhật: " + e.getMessage());
        }

        // 5. Khôi phục trạng thái ban đầu để bảo đảm tính Idempotent
        APIResponse restoreRes = request.patch("/api/v1/orders/" + orderId + "/status?newStatus=" + initialStatus);
        assertEquals(200, restoreRes.status(), "Khôi phục trạng thái đơn hàng phải trả về 200 OK");
    }

    @Test
    @Order(9)
    @DisplayName("TC_INT_03: Kiểm thử cơ chế chịu lỗi Dual-Mode Fallback khi Backend Offline")
    void tc_int_03_verifyDualModeFaultToleranceFallback() {
        // Tạo một trang mới và cấu hình chặn toàn bộ kết nối tới Backend Spring Boot
        Page fallbackPage = context.newPage();
        fallbackPage.route("**/api/v1/**", Route::abort);

        try {
            fallbackPage.navigate(BASE_URL);

            // Assert giao diện không bị trắng trang (White Screen)
            assertTrue(fallbackPage.locator(".app-wrapper").isVisible(),
                    "Giao diện ứng dụng phải hiển thị bình thường, không bị màn hình trắng");

            // Chờ cơ chế Dual-Mode kích hoạt cảnh báo Mock Data
            fallbackPage.waitForSelector("#dbStatusLabel:has-text('Mock Data')",
                    new Page.WaitForSelectorOptions().setTimeout(10000));

            String badgeText = fallbackPage.textContent("#dbStatusLabel");
            assertTrue(badgeText.contains("Mock Data"),
                    "Header phải hiển thị nhãn Mock Data khi backend bị chặn");
            assertTrue(badgeText.contains("(Cổng 8080 Offline)"),
                    "Header phải cảnh báo rõ ràng (Cổng 8080 Offline)");

            // Assert số liệu giả lập dự phòng được nạp từ EcomMockData
            assertTrue(fallbackPage.locator("#kpiTotalRevenue").isVisible(),
                    "Thẻ doanh thu vẫn hiển thị số liệu dự phòng");
        } finally {
            fallbackPage.close();
        }
    }
}
