# EcomFlow Admin 🚀

> **Hệ Thống Quản Trị Chuỗi Bán Lẻ & Thương Mại Điện Tử Đa Kênh**  
> *Dự án đang trong quá trình xây dựng và phát triển (Work in Progress).*

---

## 📌 Giới thiệu dự án

**EcomFlow Admin** là nền tảng web quản trị chuỗi bán lẻ đa kênh kết hợp thương mại điện tử quy mô lớn. Hệ thống quản lý toàn diện 16 phân hệ dữ liệu lõi kết nối với Microsoft SQL Server 2025 (`ecomflow_db`), bao gồm:
* **Hàng hóa & Danh mục:** Quản lý sản phẩm gốc, ngành hàng, thương hiệu và điểm số đánh giá.
* **Tồn kho đa chi nhánh:** Điều phối tồn kho phân bổ theo mạng lưới 51 Showroom trên toàn quốc.
* **Bán hàng & Đối soát:** Quản lý đơn hàng, chi tiết đơn và đối soát giao dịch tài chính 1:1.
* **Khuyến mãi:** Quản lý các chiến dịch ưu đãi và danh sách sản phẩm áp dụng giảm giá.
* **Khách hàng & Giỏ hàng:** Quản lý tài khoản khách hàng và theo dõi giỏ hàng chờ xử lý.
* **Nhân sự & Đánh giá:** Quản lý nhân viên chi nhánh và kiểm duyệt đánh giá khách hàng (sắc thái cảm xúc NLP).

---

## 🛠️ Công nghệ sử dụng (Tech Stack)

* **Backend:** Java 17/21, Spring Boot 3.3.x, Spring Data JPA / Hibernate, Lombok, Maven.
* **Frontend:** HTML5, CSS3 (Red & White Design System), Bootstrap 5.3, Bootstrap Icons, Chart.js.
* **Database:** Microsoft SQL Server 2025 (`ecomflow_db`).

---

## ⚙️ Yêu cầu môi trường (Prerequisites)

* **Java Development Kit (JDK):** Phiên bản 17 hoặc 21 trở lên.
* **Microsoft SQL Server:** Phiên bản 2019/2022/2025 (hoặc SQL Server Express) đã có database `ecomflow_db`.
* **Git:** Để clone mã nguồn.
* *(Tùy chọn)* **VS Code** (kèm extension Live Server) hoặc **IntelliJ IDEA / Eclipse**.

---

## 🚀 Hướng dẫn cài đặt & Khởi chạy

### 1. Clone mã nguồn về máy
```bash
git clone https://github.com/nkatu0704dataengineer/Java-Ecommerce.git
cd Java-Ecommerce
```

### 2. Cấu hình Cơ sở dữ liệu
Mở file `src/main/resources/application.properties` và cập nhật thông tin tài khoản SQL Server của bạn nếu cần:
```properties
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ecomflow_db;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=YourPasswordHere
```

> [!NOTE]  
> Nếu bạn sử dụng **SQL Server Express**, hãy đảm bảo giao thức **TCP/IP** đã được **Enable** và đặt **TCP Port = 1433** trong *SQL Server Configuration Manager* trước khi khởi chạy.

---

### 3. Khởi chạy Backend (Spring Boot)

Mở terminal tại thư mục gốc của dự án và chạy:
```powershell
# Trên Windows (sử dụng Maven Wrapper đi kèm)
.\mvnw.cmd spring-boot:run

# Hoặc nếu máy đã cài Maven sẵn:
mvn spring-boot:run
```
*Backend sẽ lắng nghe tại:* `http://localhost:8080` (REST API: `http://localhost:8080/api/v1/...`).

---

### 4. Khởi chạy Frontend

Bạn có thể chạy Frontend bằng một trong 2 cách sau:

* **Cách 1 (Khuyên dùng):** Truy cập trực tiếp qua Spring Boot:  
  Sau khi Backend đã chạy ở Bước 3, mở trình duyệt vào:
  👉 **`http://localhost:8080`**  
  *(Mã nguồn giao diện đã được cấu hình tự động phục vụ trực tiếp từ `src/main/resources/static/`).*

* **Cách 2: Mở bằng VS Code Live Server:**  
  * Mở tệp `index.html` trong VS Code, chuột phải và chọn **Open with Live Server** (cổng `5500`).
  * Giao diện đã được tích hợp cơ chế **Auto-Sync Dual-Mode**: Khi Backend (`:8080`) online, frontend sẽ tự động nạp dữ liệu thực từ CSDL; nếu Backend offline, giao diện sẽ tạm thời hiển thị dữ liệu mẫu để xem trước layout.

---

## 📂 Cấu trúc thư mục chính

```text
Java-Ecommerce/
├── pom.xml                                  # Cấu hình Maven & dependencies Spring Boot
├── mvnw.cmd                                 # Maven Wrapper cho Windows
├── index.html                               # Trang giao diện quản trị Admin Dashboard
├── css/style.css                            # Hệ thống màu sắc Red & White và CSS tùy chỉnh
├── js/
│   ├── app.js                               # Logic frontend & tích hợp REST API Spring Boot
│   └── mock-data.js                         # Dữ liệu giả lập dự phòng khi backend offline
└── src/main/
    ├── java/com/ecomflow/admin/
    │   ├── common/                          # ApiResponse, BaseAuditEntity, GlobalExceptionHandler
    │   ├── config/                          # CorsConfig, DatabaseVerificationRunner
    │   ├── entity/                          # 16 Domain Entities & Composite Keys
    │   ├── repository/                      # 16 JPA Repositories
    │   ├── service/                         # Service Interfaces & Implementations
    │   ├── dto/                             # Request/Response DTOs
    │   └── controller/                      # RestControllers (/api/v1/...)
    └── resources/
        ├── application.properties           # Cấu hình kết nối SQL Server & Hibernate
        └── static/                          # Bản đồng bộ web frontend phục vụ trực tiếp từ Spring Boot
```

---

## 📌 Trạng thái dự án
Dự án đang trong quá trình hoàn thiện các tính năng nâng cao, tinh chỉnh trải nghiệm người dùng (UX) và tối ưu hóa hiệu năng truy vấn. Mọi đóng góp và phản hồi đều được hoan nghênh!
