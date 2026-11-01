/**
 * EcomFlow Admin - Mock Data Engine
 * Ánh xạ 100% thuộc tính và quy chuẩn kiểu dữ liệu từ Microsoft SQL Server (ecomflow_db)
 * Quy mô: 16 Bảng dữ liệu liên kết chuẩn xác quan hệ Khóa ngoại (PK/FK)
 */

const EcomMockData = {
  // 1. Categories (8 ngành hàng lõi)
  categories: [
    { cate_id: "CG001", cate_name: "Phones & Tablets", icon: "bi-phone", total_products: 142 },
    { cate_id: "CG002", cate_name: "Laptops", icon: "bi-laptop", total_products: 118 },
    { cate_id: "CG003", cate_name: "Audio & Recording", icon: "bi-headphones", total_products: 86 },
    { cate_id: "CG004", cate_name: "Watches & Cameras", icon: "bi-smartwatch", total_products: 74 },
    { cate_id: "CG005", cate_name: "Appliances & Beauty", icon: "bi-gem", total_products: 65 },
    { cate_id: "CG006", cate_name: "Accessories", icon: "bi-usb-plug", total_products: 95 },
    { cate_id: "CG007", cate_name: "PCs, Monitors & Printers", icon: "bi-display", total_products: 82 },
    { cate_id: "CG008", cate_name: "TVs & Electronics", icon: "bi-tv", total_products: 50 }
  ],

  // 2. Brands (Thương hiệu công nghệ hàng đầu)
  brands: [
    { brand_id: "BR001", brand_name: "Samsung", brand_country: "South Korea" },
    { brand_id: "BR002", brand_name: "Apple", brand_country: "United States" },
    { brand_id: "BR003", brand_name: "Xiaomi", brand_country: "China" },
    { brand_id: "BR004", brand_name: "Asus", brand_country: "Taiwan" },
    { brand_id: "BR005", brand_name: "Sony", brand_country: "Japan" },
    { brand_id: "BR006", brand_name: "Dell", brand_country: "United States" },
    { brand_id: "BR007", brand_name: "HP", brand_country: "United States" },
    { brand_id: "BR008", brand_name: "Lenovo", brand_country: "China" },
    { brand_id: "BR009", brand_name: "Logitech", brand_country: "Switzerland" },
    { brand_id: "BR010", brand_name: "Bose", brand_country: "United States" }
  ],

  // 3. Subsidiaries (Mạng lưới Showroom / Chi nhánh toàn quốc - Trích 10/51 chi nhánh tiêu biểu)
  subsidiaries: [
    { sub_id: "SB001", sub_name: "EcomFlow Flagship Nguyễn Huệ", sub_city: "TP. Hồ Chí Minh", sub_district: "Quận 1", sub_street: "Số 68 Nguyễn Huệ", phone_number: "02838221001", establishment_date: "2021-03-15" },
    { sub_id: "SB002", sub_name: "EcomFlow Cầu Giấy Hub", sub_city: "Hà Nội", sub_district: "Cầu Giấy", sub_street: "Số 182 Xuân Thủy", phone_number: "02437552002", establishment_date: "2021-06-20" },
    { sub_id: "SB003", sub_name: "EcomFlow Hải Châu Center", sub_city: "Đà Nẵng", sub_district: "Hải Châu", sub_street: "Số 45 Bạch Đằng", phone_number: "02363883003", establishment_date: "2022-01-10" },
    { sub_id: "SB004", sub_name: "EcomFlow Landmark 81 Showroom", sub_city: "TP. Hồ Chí Minh", sub_district: "Bình Thạnh", sub_street: "Tầng L2 Vincom Landmark 81", phone_number: "02839114004", establishment_date: "2022-04-12" },
    { sub_id: "SB005", sub_name: "EcomFlow Hoàn Kiếm Boutique", sub_city: "Hà Nội", sub_district: "Hoàn Kiếm", sub_street: "Số 12 Tràng Thi", phone_number: "02438225005", establishment_date: "2022-08-01" },
    { sub_id: "SB006", sub_name: "EcomFlow Vạn Hạnh Mall Hub", sub_city: "TP. Hồ Chí Minh", sub_district: "Quận 10", sub_street: "Số 11 Sư Vạn Hạnh", phone_number: "02838666006", establishment_date: "2022-11-20" },
    { sub_id: "SB007", sub_name: "EcomFlow Ninh Kiều Mega Store", sub_city: "Cần Thơ", sub_district: "Ninh Kiều", sub_street: "Số 82 30 Tháng 4", phone_number: "02923777007", establishment_date: "2023-02-14" },
    { sub_id: "SB008", sub_name: "EcomFlow Biên Hòa Store", sub_city: "Đồng Nai", sub_district: "Biên Hòa", sub_street: "Số 154 Phạm Văn Thuận", phone_number: "02513888008", establishment_date: "2023-05-18" },
    { sub_id: "SB009", sub_name: "EcomFlow Thủ Đức Innovation", sub_city: "TP. Hồ Chí Minh", sub_district: "TP. Thủ Đức", sub_street: "Số 216 Võ Văn Ngân", phone_number: "02837229009", establishment_date: "2023-09-09" },
    { sub_id: "SB010", sub_name: "EcomFlow Đống Đa Experience", sub_city: "Hà Nội", sub_district: "Đống Đa", sub_street: "Số 98 Thái Hà", phone_number: "02435331010", establishment_date: "2023-11-25" }
  ],

  // 4. Products (Sản phẩm gốc kèm kích thước logistics và điểm đánh giá)
  products: [
    {
      product_id: "PR001",
      product_name: "Samsung Galaxy S24 Ultra 5G (Titanium Gray, 512GB)",
      brand_id: "BR001",
      category_id: "CG001",
      product_version: "2024 V1",
      product_description: "Flagship cao cấp với khung viền Titanium, tích hợp bút S-Pen, vi xử lý Snapdragon 8 Gen 3 for Galaxy và hệ thống camera 200MP.",
      product_status: "ACTIVE",
      product_listprice: 33990000,
      product_rating: 4.8,
      review_count: 142,
      product_weight_g: 232,
      product_length_cm: 16.23,
      product_width_cm: 7.9,
      product_height_cm: 0.86,
      product_photos_link: "https://images.unsplash.com/photo-1610945265064-0e34e5519bbf?w=500&auto=format&fit=crop&q=60"
    },
    {
      product_id: "PR002",
      product_name: "Apple MacBook Pro 14 M3 Max (36GB / 1TB SSD)",
      brand_id: "BR002",
      category_id: "CG002",
      product_version: "2024 V2",
      product_description: "Hiệu năng đồ họa đỉnh cao với chip M3 Max 14-core CPU, màn hình Liquid Retina XDR 120Hz và thời lượng pin lên đến 18 giờ.",
      product_status: "ACTIVE",
      product_listprice: 79990000,
      product_rating: 4.9,
      review_count: 88,
      product_weight_g: 1620,
      product_length_cm: 31.26,
      product_width_cm: 22.12,
      product_height_cm: 1.55,
      product_photos_link: "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=500&auto=format&fit=crop&q=60"
    },
    {
      product_id: "PR003",
      product_name: "Sony WH-1000XM5 Chống Ồn Chủ Động (Silver)",
      brand_id: "BR005",
      category_id: "CG003",
      product_version: "2023 V1",
      product_description: "Tai nghe chống ồn không dây hàng đầu với 8 micro và 2 bộ xử lý Auto NC Optimizer, hỗ trợ codec LDAC độ phân giải cao.",
      product_status: "ACTIVE",
      product_listprice: 8490000,
      product_rating: 4.7,
      review_count: 104,
      product_weight_g: 250,
      product_length_cm: 22.5,
      product_width_cm: 20.0,
      product_height_cm: 7.0,
      product_photos_link: "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&auto=format&fit=crop&q=60"
    },
    {
      product_id: "PR004",
      product_name: "Dell UltraSharp U2724DE 27-inch 2K QHD IPS Black",
      brand_id: "BR006",
      category_id: "CG007",
      product_version: "2024 V1",
      product_description: "Màn hình đồ họa chuyên nghiệp công nghệ IPS Black tỷ lệ tương phản 2000:1, tần số quét 120Hz, tích hợp Thunderbolt 4 Hub 90W.",
      product_status: "ACTIVE",
      product_listprice: 13990000,
      product_rating: 4.6,
      review_count: 53,
      product_weight_g: 5800,
      product_length_cm: 61.2,
      product_width_cm: 35.6,
      product_height_cm: 18.0,
      product_photos_link: "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=500&auto=format&fit=crop&q=60"
    },
    {
      product_id: "PR005",
      product_name: "Apple Watch Ultra 2 GPS + Cellular 49mm Titanium",
      brand_id: "BR002",
      category_id: "CG004",
      product_version: "2024 V1",
      product_description: "Đồng hồ thám hiểm chuyên nghiệp với độ sáng màn hình 3000 nits, chip S9 SiP và thời lượng pin mở rộng đến 72 giờ ở chế độ tiết kiệm.",
      product_status: "ACTIVE",
      product_listprice: 21490000,
      product_rating: 4.9,
      review_count: 76,
      product_weight_g: 61.4,
      product_length_cm: 4.9,
      product_width_cm: 4.4,
      product_height_cm: 1.44,
      product_photos_link: "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500&auto=format&fit=crop&q=60"
    },
    {
      product_id: "PR006",
      product_name: "Asus ROG Zephyrus G16 OLED (RTX 4080 / Intel Core Ultra 9)",
      brand_id: "BR004",
      category_id: "CG002",
      product_version: "2024 V1",
      product_description: "Laptop gaming cao cấp siêu mỏng nhẹ với màn hình ROG Nebula OLED 2.5K 240Hz 0.2ms, tản nhiệt buồng hơi Tri-Fan tiên tiến.",
      product_status: "ACTIVE",
      product_listprice: 74990000,
      product_rating: 4.8,
      review_count: 39,
      product_weight_g: 1850,
      product_length_cm: 35.4,
      product_width_cm: 24.6,
      product_height_cm: 1.49,
      product_photos_link: "https://images.unsplash.com/photo-1588872657578-7efd1f1555ed?w=500&auto=format&fit=crop&q=60"
    },
    {
      product_id: "PR007",
      product_name: "Logitech MX Master 3S Wireless Performance Mouse",
      brand_id: "BR009",
      category_id: "CG006",
      product_version: "2023 V2",
      product_description: "Chuột công thái học cao cấp với công tắc Quiet Clicks êm ái, cảm biến quang học 8000 DPI trên mọi bề mặt và con lăn MagSpeed điện từ.",
      product_status: "ACTIVE",
      product_listprice: 2490000,
      product_rating: 4.9,
      review_count: 215,
      product_weight_g: 141,
      product_length_cm: 12.49,
      product_width_cm: 8.43,
      product_height_cm: 5.1,
      product_photos_link: "https://images.unsplash.com/photo-1615663245857-ac93bb7c39e7?w=500&auto=format&fit=crop&q=60"
    },
    {
      product_id: "PR008",
      product_name: "Bose Smart Soundbar 900 Dolby Atmos (Black)",
      brand_id: "BR010",
      category_id: "CG008",
      product_version: "2023 V1",
      product_description: "Loa thanh rạp hát tại gia đỉnh cao với công nghệ PhaseGuide và định dạng Dolby Atmos không gian 3D chân thực, hỗ trợ AirPlay 2.",
      product_status: "ACTIVE",
      product_listprice: 23990000,
      product_rating: 4.7,
      review_count: 62,
      product_weight_g: 5750,
      product_length_cm: 104.5,
      product_width_cm: 10.7,
      product_height_cm: 5.82,
      product_photos_link: "https://images.unsplash.com/photo-1545454675-3531b543be5d?w=500&auto=format&fit=crop&q=60"
    }
  ],

  // 5. Sub_Products (Ma trận tồn kho phân bổ theo Chi nhánh - Sản phẩm)
  sub_products: [
    { subsidiary_id: "SB001", product_id: "PR001", quantity: 24, last_updated: "2026-10-30 08:30" },
    { subsidiary_id: "SB001", product_id: "PR002", quantity: 8, last_updated: "2026-10-31 09:15" },
    { subsidiary_id: "SB001", product_id: "PR003", quantity: 15, last_updated: "2026-10-28 14:00" },
    { subsidiary_id: "SB001", product_id: "PR004", quantity: 2, last_updated: "2026-11-01 10:20" }, // Cảnh báo < 5
    { subsidiary_id: "SB001", product_id: "PR005", quantity: 0, last_updated: "2026-11-01 11:00" }, // Cháy hàng
    { subsidiary_id: "SB001", product_id: "PR006", quantity: 4, last_updated: "2026-10-29 16:45" }, // Cảnh báo < 5
    { subsidiary_id: "SB001", product_id: "PR007", quantity: 45, last_updated: "2026-10-25 11:10" },
    { subsidiary_id: "SB001", product_id: "PR008", quantity: 6, last_updated: "2026-10-27 17:00" },

    { subsidiary_id: "SB002", product_id: "PR001", quantity: 18, last_updated: "2026-10-30 11:20" },
    { subsidiary_id: "SB002", product_id: "PR002", quantity: 3, last_updated: "2026-10-31 16:30" }, // Cảnh báo < 5
    { subsidiary_id: "SB002", product_id: "PR003", quantity: 21, last_updated: "2026-10-26 10:00" },
    { subsidiary_id: "SB002", product_id: "PR004", quantity: 9, last_updated: "2026-10-29 14:15" },
    { subsidiary_id: "SB002", product_id: "PR005", quantity: 7, last_updated: "2026-10-28 13:40" },
    { subsidiary_id: "SB002", product_id: "PR006", quantity: 1, last_updated: "2026-11-01 08:50" }, // Cảnh báo < 5
    { subsidiary_id: "SB002", product_id: "PR007", quantity: 32, last_updated: "2026-10-24 15:30" },
    { subsidiary_id: "SB002", product_id: "PR008", quantity: 0, last_updated: "2026-10-31 18:00" }, // Cháy hàng

    { subsidiary_id: "SB003", product_id: "PR001", quantity: 12, last_updated: "2026-10-29 09:00" },
    { subsidiary_id: "SB003", product_id: "PR002", quantity: 0, last_updated: "2026-10-30 14:10" }, // Cháy hàng
    { subsidiary_id: "SB003", product_id: "PR003", quantity: 10, last_updated: "2026-10-28 11:30" },
    { subsidiary_id: "SB003", product_id: "PR004", quantity: 4, last_updated: "2026-10-31 17:20" },
    { subsidiary_id: "SB003", product_id: "PR005", quantity: 11, last_updated: "2026-10-27 16:00" },
    { subsidiary_id: "SB003", product_id: "PR006", quantity: 0, last_updated: "2026-11-01 07:45" },
    { subsidiary_id: "SB003", product_id: "PR007", quantity: 28, last_updated: "2026-10-25 10:15" },
    { subsidiary_id: "SB003", product_id: "PR008", quantity: 5, last_updated: "2026-10-26 12:00" }
  ],

  // 6. Customers (2.402 khách hàng phân hạng)
  customers: [
    { acc_id: "ACC00001001", cus_id: "CUS001", first_name: "Hoàng Long", last_name: "Nguyễn", email: "long.nguyen@gmail.com", phone_number: "0908123456", city: "TP. Hồ Chí Minh", district: "Quận 1", customer_rank: "DIAMOND", is_active: true, account_age_days: 720, total_spent: 184500000 },
    { acc_id: "ACC00001002", cus_id: "CUS002", first_name: "Mai Anh", last_name: "Trần", email: "maianh.tran@yahoo.com", phone_number: "0912345678", city: "Hà Nội", district: "Cầu Giấy", customer_rank: "VIP", is_active: true, account_age_days: 430, total_spent: 96200000 },
    { acc_id: "ACC00001003", cus_id: "CUS003", first_name: "Quốc Bảo", last_name: "Lê", email: "bao.le@outlook.com", phone_number: "0987654321", city: "Đà Nẵng", district: "Hải Châu", customer_rank: "VIP", is_active: true, account_age_days: 310, total_spent: 81400000 },
    { acc_id: "ACC00001004", cus_id: "CUS004", first_name: "Thùy Dung", last_name: "Phạm", email: "thuydung.p@gmail.com", phone_number: "0934567890", city: "TP. Hồ Chí Minh", district: "Bình Thạnh", customer_rank: "COPPER", is_active: true, account_age_days: 120, total_spent: 24500000 },
    { acc_id: "ACC00001005", cus_id: "CUS005", first_name: "Văn Hùng", last_name: "Đỗ", email: "hung.do@company.vn", phone_number: "0971122334", city: "Hà Nội", district: "Đống Đa", customer_rank: "COPPER", is_active: false, account_age_days: 85, total_spent: 12000000 },
    { acc_id: "ACC00001006", cus_id: "CUS006", first_name: "Bích Phương", last_name: "Vũ", email: "phuong.vu@gmail.com", phone_number: "0945566778", city: "TP. Hồ Chí Minh", district: "Quận 7", customer_rank: "DIAMOND", is_active: true, account_age_days: 890, total_spent: 230000000 }
  ],

  // 7. Orders & Order Items & Payments (Đơn hàng và đối soát tài chính 1:1)
  orders: [
    {
      order_id: "OD000001",
      acc_id: "ACC00001001",
      customer_name: "Nguyễn Hoàng Long",
      employee_id: "EP141",
      order_status: "DELIVERED",
      total_amount: 63894000,
      total_shipfee: 150000,
      target_street: "Số 15 Lê Duẩn, P. Bến Nghé",
      target_district: "Quận 1",
      target_city: "TP. Hồ Chí Minh",
      created_at: "2026-10-28 14:32:00",
      delivered_at: "2026-10-30 11:20:00",
      items: [
        { order_item_id: 1, product_id: "PR001", product_name: "Samsung Galaxy S24 Ultra 5G", subsidiary_id: "SB001", subsidiary_name: "EcomFlow Nguyễn Huệ", quantity: 1, list_price: 33990000, discount_rate: 0.1, sale_price: 30591000, total_price: 30591000, sale_id: "SE001", shipping_fee: 75000 },
        { order_item_id: 2, product_id: "PR002", product_name: "MacBook Pro 14 M3 Max", subsidiary_id: "SB001", subsidiary_name: "EcomFlow Nguyễn Huệ", quantity: 1, list_price: 79990000, discount_rate: 0.05, sale_price: 75990500, total_price: 75990500, sale_id: "SE002", shipping_fee: 75000 }
      ],
      payment: {
        payment_id: "PAY000001",
        order_id: "OD000001",
        payment_method: "VNPAY",
        payment_status: "SUCCESS",
        payment_amount: 63894000,
        paid_at: "2026-10-28 14:35:10"
      }
    },
    {
      order_id: "OD000002",
      acc_id: "ACC00001002",
      customer_name: "Trần Mai Anh",
      employee_id: "EP308",
      order_status: "DELIVERED",
      total_amount: 49046400,
      total_shipfee: 80000,
      target_street: "Tòa Keangnam Landmark 72",
      target_district: "Cầu Giấy",
      target_city: "Hà Nội",
      created_at: "2026-10-29 09:15:00",
      delivered_at: "2026-10-31 16:45:00",
      items: [
        { order_item_id: 1, product_id: "PR005", product_name: "Apple Watch Ultra 2 GPS + Cellular", subsidiary_id: "SB002", subsidiary_name: "EcomFlow Cầu Giấy Hub", quantity: 2, list_price: 21490000, discount_rate: 0.0, sale_price: 21490000, total_price: 42980000, sale_id: null, shipping_fee: 80000 }
      ],
      payment: {
        payment_id: "PAY000002",
        order_id: "OD000002",
        payment_method: "MOMO",
        payment_status: "SUCCESS",
        payment_amount: 49046400,
        paid_at: "2026-10-29 09:16:30"
      }
    },
    {
      order_id: "OD000003",
      acc_id: "ACC00001003",
      customer_name: "Lê Quốc Bảo",
      employee_id: "EP253",
      order_status: "SHIPPED",
      total_amount: 16480000,
      total_shipfee: 60000,
      target_street: "Số 204 Nguyễn Văn Linh",
      target_district: "Hải Châu",
      target_city: "Đà Nẵng",
      created_at: "2026-10-31 15:40:00",
      delivered_at: null,
      items: [
        { order_item_id: 1, product_id: "PR003", product_name: "Sony WH-1000XM5 Chống Ồn", subsidiary_id: "SB003", subsidiary_name: "EcomFlow Hải Châu Center", quantity: 1, list_price: 8490000, discount_rate: 0.05, sale_price: 8065500, total_price: 8065500, sale_id: "SE002", shipping_fee: 30000 },
        { order_item_id: 2, product_id: "PR007", product_name: "Logitech MX Master 3S", subsidiary_id: "SB003", subsidiary_name: "EcomFlow Hải Châu Center", quantity: 3, list_price: 2490000, discount_rate: 0.0, sale_price: 2490000, total_price: 7470000, sale_id: null, shipping_fee: 30000 }
      ],
      payment: {
        payment_id: "PAY000003",
        order_id: "OD000003",
        payment_method: "ZALOPAY",
        payment_status: "SUCCESS",
        payment_amount: 16480000,
        paid_at: "2026-10-31 15:42:00"
      }
    },
    {
      order_id: "OD000004",
      acc_id: "ACC00001004",
      customer_name: "Phạm Thùy Dung",
      employee_id: "EP060",
      order_status: "SHIPPED",
      total_amount: 74990000,
      total_shipfee: 120000,
      target_street: "Số 88 Điện Biên Phủ",
      target_district: "Bình Thạnh",
      target_city: "TP. Hồ Chí Minh",
      created_at: "2026-11-01 08:20:00",
      delivered_at: null,
      items: [
        { order_item_id: 1, product_id: "PR006", product_name: "Asus ROG Zephyrus G16 OLED", subsidiary_id: "SB004", subsidiary_name: "EcomFlow Landmark 81", quantity: 1, list_price: 74990000, discount_rate: 0.0, sale_price: 74990000, total_price: 74990000, sale_id: null, shipping_fee: 120000 }
      ],
      payment: {
        payment_id: "PAY000004",
        order_id: "OD000004",
        payment_method: "BANKING",
        payment_status: "PENDING",
        payment_amount: 74990000,
        paid_at: null
      }
    }
  ],

  // 8. Program_Sales (Chiến dịch ưu đãi)
  program_sales: [
    { sale_id: "SE001", sale_name: "Đại Tiệc Siêu Phẩm Công Nghệ Đầu Năm", discount_value: 0.10, min_ordervalue: 1000000, start_date: "2026-01-01", end_date: "2026-12-31", status: "ACTIVE", assigned_count: 24 },
    { sale_id: "SE002", sale_name: "Ưu Đãi Âm Thanh Sống Động - Music Festival", discount_value: 0.05, min_ordervalue: 500000, start_date: "2026-06-01", end_date: "2026-11-30", status: "ACTIVE", assigned_count: 18 },
    { sale_id: "SE003", sale_name: "Tuần Lễ Laptop Doanh Nhân & Đồ Họa Pro", discount_value: 0.15, min_ordervalue: 20000000, start_date: "2026-10-15", end_date: "2026-11-15", status: "ACTIVE", assigned_count: 32 },
    { sale_id: "SE004", sale_name: "Mega Sale 11.11 - Siêu Bão Công Nghệ", discount_value: 0.25, min_ordervalue: 3000000, start_date: "2026-11-10", end_date: "2026-11-15", status: "SCHEDULED", assigned_count: 55 },
    { sale_id: "SE005", sale_name: "Black Friday Siêu Deal Giờ Vàng", discount_value: 0.30, min_ordervalue: 5000000, start_date: "2026-11-25", end_date: "2026-11-29", status: "SCHEDULED", assigned_count: 60 }
  ],

  // 9. Carts & Cart Items (Giỏ hàng khách lưu trữ)
  carts: [
    {
      cart_id: "CART001",
      acc_id: "ACC00001001",
      customer_name: "Nguyễn Hoàng Long",
      cart_status: "ACTIVE",
      total_items: 2,
      total_amount: 16480000,
      created_at: "2026-10-31 20:15:00",
      items: [
        { item_sequence: 1, product_id: "PR003", product_name: "Sony WH-1000XM5 Chống Ồn", quantity: 1, list_price: 8490000, item_total_amount: 8490000 },
        { item_sequence: 2, product_id: "PR007", product_name: "Logitech MX Master 3S Wireless", quantity: 2, list_price: 2490000, item_total_amount: 4980000 }
      ]
    },
    {
      cart_id: "CART002",
      acc_id: "ACC00001004",
      customer_name: "Phạm Thùy Dung",
      cart_status: "ACTIVE",
      total_items: 1,
      total_amount: 13990000,
      created_at: "2026-11-01 10:05:00",
      items: [
        { item_sequence: 1, product_id: "PR004", product_name: "Dell UltraSharp U2724DE 27-inch 2K QHD", quantity: 1, list_price: 13990000, item_total_amount: 13990000 }
      ]
    }
  ],

  // 10. Reviews & Sentiments (5.459 đánh giá có phân tích sắc thái NLP)
  reviews: [
    {
      review_id: "RV000001",
      order_id: "OD000001",
      product_id: "PR001",
      product_name: "Samsung Galaxy S24 Ultra 5G",
      customer_name: "Nguyễn Hoàng Long",
      review_score: 5.0,
      review_sentiment: "Positive",
      review_status: "PUBLISHED",
      review_title: "Chất lượng máy trên cả tuyệt vời!",
      review_message: "Máy cầm đầm tay, màn hình phẳng rất sáng và nét. Giao hàng từ chi nhánh Nguyễn Huệ chỉ mất 2 tiếng đã nhận được máy. Rất hài lòng với dịch vụ EcomFlow!",
      created_at: "2026-10-30 15:20:00",
      replied_at: "2026-10-30 16:00:00",
      has_reply: true,
      is_positive_review: true,
      is_neutral_review: false,
      is_negative_review: false,
      is_missing_comment: false
    },
    {
      review_id: "RV000002",
      order_id: "OD000002",
      product_id: "PR005",
      product_name: "Apple Watch Ultra 2 GPS + Cellular",
      customer_name: "Trần Mai Anh",
      review_score: 4.5,
      review_sentiment: "Positive",
      review_status: "PUBLISHED",
      review_title: "Pin trâu, kiểu dáng rất thể thao",
      review_message: "Pin dùng liên tục 3 ngày mới phải sạc. Quai đeo êm tay, nghe gọi rõ ràng khi đang đi xe máy.",
      created_at: "2026-10-31 18:10:00",
      replied_at: null,
      has_reply: false,
      is_positive_review: true,
      is_neutral_review: false,
      is_negative_review: false,
      is_missing_comment: false
    },
    {
      review_id: "RV000003",
      order_id: "OD000003",
      product_id: "PR003",
      product_name: "Sony WH-1000XM5 Chống Ồn",
      customer_name: "Lê Quốc Bảo",
      review_score: 2.0,
      review_sentiment: "Negative",
      review_status: "PUBLISHED",
      review_title: "Hộp bị móp góc khi vận chuyển",
      review_message: "Tai nghe hoạt động bình thường nhưng hộp bên ngoài bị cấn góc khá nặng, mong bên vận chuyển cẩn thận hơn.",
      created_at: "2026-11-01 09:30:00",
      replied_at: null,
      has_reply: false,
      is_positive_review: false,
      is_neutral_review: false,
      is_negative_review: true,
      is_missing_comment: false
    },
    {
      review_id: "RV000004",
      order_id: "OD000004",
      product_id: "PR006",
      product_name: "Asus ROG Zephyrus G16 OLED",
      customer_name: "Phạm Thùy Dung",
      review_score: 3.0,
      review_sentiment: "Neutral",
      review_status: "PUBLISHED",
      review_title: "Máy ấm nhanh khi render nặng",
      review_message: "Thiết kế đẹp mỏng nhẹ, màn OLED rực rỡ nhưng quạt hú hơi to lúc render file 4K.",
      created_at: "2026-11-01 11:15:00",
      replied_at: null,
      has_reply: false,
      is_positive_review: false,
      is_neutral_review: true,
      is_negative_review: false,
      is_missing_comment: false
    }
  ],

  // 11. Employees (409 nhân sự trực thuộc 51 chi nhánh)
  employees: [
    { employee_id: "EP141", first_name: "Minh Tuấn", last_name: "Lê", email: "tuan.le@ecomflow.vn", phone: "0901234141", role: "Store Manager", subsidiary_id: "SB001", subsidiary_name: "EcomFlow Flagship Nguyễn Huệ", recruited_at: "2021-04-01" },
    { employee_id: "EP308", first_name: "Thị Hằng", last_name: "Nguyễn", email: "hang.nguyen@ecomflow.vn", phone: "0902345308", role: "Cashier", subsidiary_id: "SB002", subsidiary_name: "EcomFlow Cầu Giấy Hub", recruited_at: "2022-02-15" },
    { employee_id: "EP253", first_name: "Đức Trí", last_name: "Võ", email: "tri.vo@ecomflow.vn", phone: "0903456253", role: "Sales Consultant", subsidiary_id: "SB003", subsidiary_name: "EcomFlow Hải Châu Center", recruited_at: "2022-07-20" },
    { employee_id: "EP060", first_name: "Thành Đạt", last_name: "Trần", email: "dat.tran@ecomflow.vn", phone: "0904567060", role: "Stock Keeper", subsidiary_id: "SB004", subsidiary_name: "EcomFlow Landmark 81", recruited_at: "2023-01-10" },
    { employee_id: "EP089", first_name: "Phương Linh", last_name: "Đặng", email: "linh.dang@ecomflow.vn", phone: "0905678089", role: "Technical Support", subsidiary_id: "SB001", subsidiary_name: "EcomFlow Flagship Nguyễn Huệ", recruited_at: "2023-08-01" }
  ]
};

// Export to Global Window for browser usage & module for Node if needed
if (typeof window !== "undefined") {
  window.EcomMockData = EcomMockData;
}
if (typeof module !== "undefined" && module.exports) {
  module.exports = EcomMockData;
}
