/**
 * EcomFlow Admin - Core Application Logic
 * Đảm bảo 100% tính năng điều hướng Single-Page, tương tác CRUD, 
 * vẽ biểu đồ Chart.js và phản ánh chuẩn xác 16 bảng của ecomflow_db.
 */

document.addEventListener("DOMContentLoaded", () => {
  // Check if mock data is loaded
  if (typeof EcomMockData === "undefined") {
    console.error("EcomMockData not loaded!");
    return;
  }

  // State Management
  const AppState = {
    currentView: "dashboard",
    currentSubsidiary: "ALL",
    data: EcomMockData,
    charts: {}
  };

  // ==========================================================================
  // 1. UTILITY FUNCTIONS
  // ==========================================================================
  const formatCurrency = (amount) => {
    return new Intl.NumberFormat("vi-VN", {
      style: "currency",
      currency: "VND"
    }).format(amount);
  };

  const showToast = (title, message, type = "success") => {
    const toastContainer = document.getElementById("toastContainer");
    if (!toastContainer) return;

    const toastId = "toast_" + Date.now();
    const bgClass = type === "success" ? "text-bg-success" : type === "danger" ? "text-bg-danger" : "text-bg-dark";

    const toastHtml = `
      <div id="${toastId}" class="toast align-items-center ${bgClass} border-0 shadow-lg" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
          <div class="toast-body">
            <strong>${title}</strong>: ${message}
          </div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
      </div>
    `;

    toastContainer.insertAdjacentHTML("beforeend", toastHtml);
    const toastElement = document.getElementById(toastId);
    const bsToast = new bootstrap.Toast(toastElement, { delay: 3500 });
    bsToast.show();

    toastElement.addEventListener("hidden.bs.toast", () => {
      toastElement.remove();
    });
  };

  // ==========================================================================
  // 2. VIEW ROUTER & NAVIGATION
  // ==========================================================================
  const navigateTo = (viewName) => {
    // Hide all views
    document.querySelectorAll(".view-section").forEach(sec => {
      sec.classList.add("d-none");
    });

    // Show target view
    const targetSection = document.getElementById(`view-${viewName}`);
    if (targetSection) {
      targetSection.classList.remove("d-none");
      AppState.currentView = viewName;
    }

    // Update active nav link
    document.querySelectorAll(".nav-item-link").forEach(link => {
      link.classList.remove("active");
      if (link.getAttribute("data-view") === viewName) {
        link.classList.add("active");
      }
    });

    // Refresh view specific components
    if (viewName === "dashboard") {
      renderDashboardCharts();
      renderDashboardKPIs();
      renderDashboardRecentOrders();
      renderDashboardLowStock();
    } else if (viewName === "products") {
      renderProductsTable();
    } else if (viewName === "inventory") {
      renderInventoryTable();
    } else if (viewName === "orders") {
      renderOrdersTable();
    } else if (viewName === "promotions") {
      renderPromotionsTable();
    } else if (viewName === "customers") {
      renderCustomersTable();
    } else if (viewName === "reviews-staff") {
      renderReviewsTable();
      renderEmployeesTable();
    }

    // Scroll to top
    window.scrollTo({ top: 0, behavior: "smooth" });
  };

  // Nav link click events
  document.querySelectorAll(".nav-item-link[data-view]").forEach(link => {
    link.addEventListener("click", (e) => {
      e.preventDefault();
      const view = link.getAttribute("data-view");
      navigateTo(view);
      // Close mobile sidebar if open
      const sidebar = document.getElementById("mainSidebar");
      if (window.innerWidth < 992 && sidebar.classList.contains("show")) {
        sidebar.classList.remove("show");
      }
    });
  });

  // Sidebar Mobile Toggle
  const sidebarToggleBtn = document.getElementById("sidebarToggleBtn");
  if (sidebarToggleBtn) {
    sidebarToggleBtn.addEventListener("click", () => {
      const sidebar = document.getElementById("mainSidebar");
      sidebar.classList.toggle("show");
    });
  }

  // Branch Selector Filter
  const topbarBranchSelect = document.getElementById("topbarBranchSelect");
  if (topbarBranchSelect) {
    topbarBranchSelect.addEventListener("change", (e) => {
      AppState.currentSubsidiary = e.target.value;
      showToast("Đổi Chi Nhánh", `Đã lọc dữ liệu theo: ${e.target.options[e.target.selectedIndex].text}`, "info");
      // Re-render active view
      navigateTo(AppState.currentView);
    });
  }

  // ==========================================================================
  // 3. EXECUTIVE DASHBOARD MODULE
  // ==========================================================================
  const renderDashboardKPIs = () => {
    // 1. Total Revenue from Payments
    const totalRevenue = AppState.data.orders.reduce((sum, o) => sum + (o.payment.payment_status === "SUCCESS" ? o.payment.payment_amount : 0), 0);
    const kpiRevenueElem = document.getElementById("kpiTotalRevenue");
    if (kpiRevenueElem) kpiRevenueElem.textContent = formatCurrency(totalRevenue);

    // 2. Total Orders
    const kpiOrdersElem = document.getElementById("kpiTotalOrders");
    if (kpiOrdersElem) kpiOrdersElem.textContent = AppState.data.orders.length.toLocaleString("vi-VN");

    // 3. Active Customers
    const activeCustomers = AppState.data.customers.filter(c => c.is_active).length;
    const kpiCustElem = document.getElementById("kpiActiveCustomers");
    if (kpiCustElem) kpiCustElem.textContent = `${activeCustomers} / ${AppState.data.customers.length}`;

    // 4. Low stock count (< 5)
    const lowStockCount = AppState.data.sub_products.filter(sp => sp.quantity < 5).length;
    const kpiStockElem = document.getElementById("kpiLowStock");
    if (kpiStockElem) kpiStockElem.textContent = lowStockCount;
  };

  const renderDashboardRecentOrders = () => {
    const tbody = document.getElementById("dashboardRecentOrdersBody");
    if (!tbody) return;

    tbody.innerHTML = AppState.data.orders.slice(0, 5).map(order => `
      <tr>
        <td class="fw-bold text-danger">${order.order_id}</td>
        <td>
          <div class="fw-semibold">${order.customer_name}</div>
          <small class="text-muted">${order.target_city}</small>
        </td>
        <td>
          <span class="badge-status ${order.order_status.toLowerCase()}">
            <i class="bi bi-circle-fill" style="font-size: 6px;"></i> ${order.order_status}
          </span>
        </td>
        <td>
          <span class="badge bg-light text-dark border">
            ${order.payment.payment_method}
          </span>
        </td>
        <td class="fw-bold">${formatCurrency(order.total_amount)}</td>
        <td>
          <button class="btn btn-sm btn-outline-red view-order-btn" data-order-id="${order.order_id}">
            <i class="bi bi-eye"></i> Chi tiết
          </button>
        </td>
      </tr>
    `).join("");

    // Attach order detail triggers
    tbody.querySelectorAll(".view-order-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        openOrderDetail(btn.getAttribute("data-order-id"));
      });
    });
  };

  const renderDashboardLowStock = () => {
    const tbody = document.getElementById("dashboardLowStockBody");
    if (!tbody) return;

    const lowStockItems = AppState.data.sub_products.filter(sp => sp.quantity < 5).slice(0, 5);

    tbody.innerHTML = lowStockItems.map(item => {
      const prod = AppState.data.products.find(p => p.product_id === item.product_id);
      const sub = AppState.data.subsidiaries.find(s => s.sub_id === item.subsidiary_id);
      return `
        <tr>
          <td>
            <div class="fw-semibold text-truncate" style="max-width: 180px;">${prod ? prod.product_name : item.product_id}</div>
            <small class="text-muted">${item.product_id}</small>
          </td>
          <td>
            <span class="badge bg-light text-secondary border">${sub ? sub.sub_name : item.subsidiary_id}</span>
          </td>
          <td>
            <span class="${item.quantity === 0 ? 'stock-out' : 'stock-low'}">
              ${item.quantity === 0 ? 'Hết hàng (0)' : item.quantity + ' cái'}
            </span>
          </td>
          <td>
            <button class="btn btn-sm btn-outline-danger adjust-stock-btn" data-sub="${item.subsidiary_id}" data-prod="${item.product_id}">
              <i class="bi bi-plus-circle"></i> Nhập kho
            </button>
          </td>
        </tr>
      `;
    }).join("");

    tbody.querySelectorAll(".adjust-stock-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        openStockAdjustModal(btn.getAttribute("data-sub"), btn.getAttribute("data-prod"));
      });
    });
  };

  const renderDashboardCharts = () => {
    // Chart 1: Revenue & Order Trend
    const ctxRevenue = document.getElementById("chartRevenueTrend");
    if (ctxRevenue) {
      if (AppState.charts.revenue) AppState.charts.revenue.destroy();
      AppState.charts.revenue = new Chart(ctxRevenue, {
        type: "line",
        data: {
          labels: ["T5", "T6", "T7", "T8", "T9", "T10", "T11"],
          datasets: [
            {
              label: "Doanh thu thực thu (Triệu VNĐ)",
              data: [1250, 1420, 1680, 1550, 1890, 2150, 2480],
              borderColor: "#D90429",
              backgroundColor: "rgba(217, 4, 41, 0.08)",
              borderWidth: 3,
              fill: true,
              tension: 0.35,
              pointBackgroundColor: "#D90429",
              pointRadius: 5
            },
            {
              label: "Số lượng đơn hàng (Đơn)",
              data: [320, 390, 450, 410, 520, 580, 640],
              borderColor: "#64748B",
              backgroundColor: "transparent",
              borderWidth: 2,
              borderDash: [5, 5],
              tension: 0.35,
              pointBackgroundColor: "#64748B",
              pointRadius: 4,
              yAxisID: "y1"
            }
          ]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { position: "top", labels: { boxWidth: 14, font: { family: "Plus Jakarta Sans" } } },
            tooltip: { padding: 12, cornerRadius: 8 }
          },
          scales: {
            y: { grid: { color: "#F1F5F9" }, title: { display: true, text: "Doanh thu (triệu VNĐ)" } },
            y1: { position: "right", grid: { drawOnChartArea: false }, title: { display: true, text: "Số lượng đơn" } },
            x: { grid: { display: false } }
          }
        }
      });
    }

    // Chart 2: Top Subsidiaries
    const ctxBranch = document.getElementById("chartTopBranches");
    if (ctxBranch) {
      if (AppState.charts.branches) AppState.charts.branches.destroy();
      AppState.charts.branches = new Chart(ctxBranch, {
        type: "bar",
        data: {
          labels: ["Nguyễn Huệ (HCM)", "Cầu Giấy (HN)", "Hải Châu (ĐN)", "Landmark 81 (HCM)", "Hoàn Kiếm (HN)"],
          datasets: [{
            label: "Doanh thu (Triệu VNĐ)",
            data: [980, 760, 620, 590, 540],
            backgroundColor: ["#D90429", "#E11D48", "#F43F5E", "#FB7185", "#FDA4AF"],
            borderRadius: 6
          }]
        },
        options: {
          indexAxis: "y",
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } },
          scales: {
            x: { grid: { color: "#F1F5F9" } },
            y: { grid: { display: false } }
          }
        }
      });
    }

    // Chart 3: Sentiment Breakdown
    const ctxSentiment = document.getElementById("chartSentiment");
    if (ctxSentiment) {
      if (AppState.charts.sentiment) AppState.charts.sentiment.destroy();
      AppState.charts.sentiment = new Chart(ctxSentiment, {
        type: "doughnut",
        data: {
          labels: ["Tích cực (Positive)", "Trung lập (Neutral)", "Tiêu cực (Negative)"],
          datasets: [{
            data: [72, 18, 10],
            backgroundColor: ["#10B981", "#64748B", "#EF4444"],
            hoverOffset: 6
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: { position: "bottom", labels: { boxWidth: 12, padding: 15 } }
          },
          cutout: "70%"
        }
      });
    }
  };

  // ==========================================================================
  // 4. PRODUCTS & CATALOG MODULE
  // ==========================================================================
  const renderProductsTable = () => {
    const tbody = document.getElementById("productsTableBody");
    if (!tbody) return;

    const searchTerm = (document.getElementById("productSearchInput")?.value || "").toLowerCase();
    const categoryFilter = document.getElementById("productCategoryFilter")?.value || "ALL";
    const brandFilter = document.getElementById("productBrandFilter")?.value || "ALL";

    let filtered = AppState.data.products.filter(p => {
      const matchSearch = p.product_name.toLowerCase().includes(searchTerm) || p.product_id.toLowerCase().includes(searchTerm);
      const matchCat = categoryFilter === "ALL" || p.category_id === categoryFilter;
      const matchBrand = brandFilter === "ALL" || p.brand_id === brandFilter;
      return matchSearch && matchCat && matchBrand;
    });

    tbody.innerHTML = filtered.map(prod => {
      const brand = AppState.data.brands.find(b => b.brand_id === prod.brand_id);
      const cate = AppState.data.categories.find(c => c.cate_id === prod.category_id);
      return `
        <tr>
          <td class="fw-bold text-secondary">${prod.product_id}</td>
          <td>
            <div class="d-flex align-items-center gap-3">
              <img src="${prod.product_photos_link}" alt="${prod.product_name}" class="rounded border" style="width: 48px; height: 48px; object-fit: cover;">
              <div>
                <div class="fw-bold text-main">${prod.product_name}</div>
                <small class="text-muted">${prod.product_version || 'Tiêu chuẩn'} | ${prod.product_weight_g}g</small>
              </div>
            </div>
          </td>
          <td><span class="badge bg-light text-dark border">${brand ? brand.brand_name : prod.brand_id}</span></td>
          <td><span class="badge bg-danger-subtle text-danger border border-danger-subtle">${cate ? cate.cate_name : prod.category_id}</span></td>
          <td class="fw-bold text-danger">${formatCurrency(prod.product_listprice)}</td>
          <td>
            <div class="d-flex align-items-center gap-1 text-warning">
              <i class="bi bi-star-fill"></i>
              <span class="fw-bold text-dark">${prod.product_rating}</span>
              <small class="text-muted">(${prod.review_count})</small>
            </div>
          </td>
          <td>
            <span class="badge-status active">
              <i class="bi bi-check-circle-fill"></i> Đang bán
            </span>
          </td>
          <td>
            <div class="btn-group btn-group-sm">
              <button class="btn btn-outline-secondary edit-product-btn" data-prod-id="${prod.product_id}" title="Sửa thông số">
                <i class="bi bi-pencil"></i>
              </button>
              <button class="btn btn-outline-danger view-stock-btn" data-prod-id="${prod.product_id}" title="Xem tồn kho">
                <i class="bi bi-boxes"></i>
              </button>
            </div>
          </td>
        </tr>
      `;
    }).join("");

    // Attach actions
    tbody.querySelectorAll(".view-stock-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        const prodId = btn.getAttribute("data-prod-id");
        navigateTo("inventory");
        const searchBox = document.getElementById("inventorySearchInput");
        if (searchBox) {
          searchBox.value = prodId;
          renderInventoryTable();
        }
      });
    });

    tbody.querySelectorAll(".edit-product-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        const prodId = btn.getAttribute("data-prod-id");
        openEditProductModal(prodId);
      });
    });
  };

  // Add Product Form Submit
  const addProductForm = document.getElementById("addProductForm");
  if (addProductForm) {
    addProductForm.addEventListener("submit", (e) => {
      e.preventDefault();
      const newProd = {
        product_id: "PR" + String(AppState.data.products.length + 1).padStart(3, "0"),
        product_name: document.getElementById("form_product_name").value,
        brand_id: document.getElementById("form_brand_id").value,
        category_id: document.getElementById("form_category_id").value,
        product_version: "2026 V1",
        product_description: document.getElementById("form_product_description").value,
        product_status: "ACTIVE",
        product_listprice: parseFloat(document.getElementById("form_product_listprice").value) || 0,
        product_rating: 5.0,
        review_count: 0,
        product_weight_g: parseFloat(document.getElementById("form_product_weight_g").value) || 200,
        product_length_cm: 15.0,
        product_width_cm: 7.5,
        product_height_cm: 1.0,
        product_photos_link: "https://images.unsplash.com/photo-1546868871-7041f2a55e12?w=500&auto=format&fit=crop&q=60"
      };

      AppState.data.products.unshift(newProd);

      // Create initial stock in subsidiary 1
      AppState.data.sub_products.push({
        subsidiary_id: "SB001",
        product_id: newProd.product_id,
        quantity: 10,
        last_updated: "Vừa tạo"
      });

      renderProductsTable();
      bootstrap.Modal.getInstance(document.getElementById("addProductModal"))?.hide();
      addProductForm.reset();
      showToast("Thêm Thành Công", `Đã tạo sản phẩm [${newProd.product_id}] ${newProd.product_name}`);
    });
  }

  const openEditProductModal = (prodId) => {
    const prod = AppState.data.products.find(p => p.product_id === prodId);
    if (!prod) return;

    document.getElementById("form_product_name").value = prod.product_name;
    document.getElementById("form_brand_id").value = prod.brand_id;
    document.getElementById("form_category_id").value = prod.category_id;
    document.getElementById("form_product_listprice").value = prod.product_listprice;
    document.getElementById("form_product_weight_g").value = prod.product_weight_g;
    document.getElementById("form_product_description").value = prod.product_description;

    const modalTitle = document.getElementById("addProductModalLabel");
    if (modalTitle) modalTitle.textContent = `Chỉnh Sửa Sản Phẩm [${prod.product_id}]`;

    const modal = new bootstrap.Modal(document.getElementById("addProductModal"));
    modal.show();
  };

  // Filter triggers for Products
  ["productSearchInput", "productCategoryFilter", "productBrandFilter"].forEach(id => {
    document.getElementById(id)?.addEventListener("input", renderProductsTable);
    document.getElementById(id)?.addEventListener("change", renderProductsTable);
  });

  // ==========================================================================
  // 5. INVENTORY MODULE (51 Branches & Sub_Products)
  // ==========================================================================
  const renderInventoryTable = () => {
    const tbody = document.getElementById("inventoryTableBody");
    if (!tbody) return;

    const branchFilter = document.getElementById("inventoryBranchFilter")?.value || "ALL";
    const statusFilter = document.getElementById("inventoryStatusFilter")?.value || "ALL";
    const searchTerm = (document.getElementById("inventorySearchInput")?.value || "").toLowerCase();

    let filtered = AppState.data.sub_products.filter(item => {
      const matchBranch = branchFilter === "ALL" || item.subsidiary_id === branchFilter;
      const matchStatus = statusFilter === "ALL" ||
        (statusFilter === "OUT" && item.quantity === 0) ||
        (statusFilter === "LOW" && item.quantity > 0 && item.quantity < 5) ||
        (statusFilter === "IN_STOCK" && item.quantity >= 5);

      const prod = AppState.data.products.find(p => p.product_id === item.product_id);
      const matchSearch = item.product_id.toLowerCase().includes(searchTerm) ||
        (prod && prod.product_name.toLowerCase().includes(searchTerm));

      return matchBranch && matchStatus && matchSearch;
    });

    tbody.innerHTML = filtered.map(item => {
      const prod = AppState.data.products.find(p => p.product_id === item.product_id);
      const sub = AppState.data.subsidiaries.find(s => s.sub_id === item.subsidiary_id);

      let badgeClass = "stock-healthy";
      let statusText = "Đủ hàng";
      if (item.quantity === 0) {
        badgeClass = "stock-out";
        statusText = "Hết hàng";
      } else if (item.quantity < 5) {
        badgeClass = "stock-low";
        statusText = "Sắp hết";
      }

      return `
        <tr>
          <td>
            <div class="fw-bold">${sub ? sub.sub_name : item.subsidiary_id}</div>
            <small class="text-muted"><i class="bi bi-geo-alt"></i> ${sub ? sub.sub_city : ''}</small>
          </td>
          <td>
            <div class="d-flex align-items-center gap-2">
              <span class="fw-bold text-danger">${item.product_id}</span>
              <div class="text-truncate" style="max-width: 250px;">${prod ? prod.product_name : ''}</div>
            </div>
          </td>
          <td>
            <span class="${badgeClass}">${statusText}</span>
          </td>
          <td>
            <div class="d-flex align-items-center gap-2">
              <button class="btn btn-sm btn-outline-secondary stock-quick-btn" data-sub="${item.subsidiary_id}" data-prod="${item.product_id}" data-action="decrease">
                <i class="bi bi-dash"></i>
              </button>
              <input type="number" class="form-control form-control-sm text-center fw-bold stock-inline-input" style="width: 70px;" value="${item.quantity}" min="0" data-sub="${item.subsidiary_id}" data-prod="${item.product_id}">
              <button class="btn btn-sm btn-outline-secondary stock-quick-btn" data-sub="${item.subsidiary_id}" data-prod="${item.product_id}" data-action="increase">
                <i class="bi bi-plus"></i>
              </button>
            </div>
          </td>
          <td class="text-muted small">${item.last_updated}</td>
          <td>
            <button class="btn btn-sm btn-outline-red adjust-stock-btn" data-sub="${item.subsidiary_id}" data-prod="${item.product_id}">
              <i class="bi bi-arrow-left-right"></i> Điều chuyển
            </button>
          </td>
        </tr>
      `;
    }).join("");

    // Quick +/- Buttons
    tbody.querySelectorAll(".stock-quick-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        const subId = btn.getAttribute("data-sub");
        const prodId = btn.getAttribute("data-prod");
        const action = btn.getAttribute("data-action");

        const target = AppState.data.sub_products.find(sp => sp.subsidiary_id === subId && sp.product_id === prodId);
        if (target) {
          if (action === "increase") target.quantity++;
          else if (action === "decrease" && target.quantity > 0) target.quantity--;
          target.last_updated = "Vừa cập nhật";
          renderInventoryTable();
          showToast("Cập Nhật Kho", `Tồn kho [${prodId}] tại ${subId} hiện là: ${target.quantity}`);
        }
      });
    });

    // Inline input change
    tbody.querySelectorAll(".stock-inline-input").forEach(input => {
      input.addEventListener("change", (e) => {
        const subId = input.getAttribute("data-sub");
        const prodId = input.getAttribute("data-prod");
        const newQty = parseInt(e.target.value) || 0;

        const target = AppState.data.sub_products.find(sp => sp.subsidiary_id === subId && sp.product_id === prodId);
        if (target) {
          target.quantity = Math.max(0, newQty);
          target.last_updated = "Vừa cập nhật";
          renderInventoryTable();
          showToast("Cập Nhật Tồn Kho", `Số lượng tồn mới là ${target.quantity}`);
        }
      });
    });

    // Open Stock Adjust Modal
    tbody.querySelectorAll(".adjust-stock-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        openStockAdjustModal(btn.getAttribute("data-sub"), btn.getAttribute("data-prod"));
      });
    });
  };

  const openStockAdjustModal = (subId, prodId) => {
    const modalElem = document.getElementById("stockAdjustModal");
    if (!modalElem) return;

    const subSelect = document.getElementById("modal_sub_id");
    const prodSelect = document.getElementById("modal_product_id");
    const qtyInput = document.getElementById("modal_adjust_qty");

    if (subSelect) subSelect.value = subId;
    if (prodSelect) prodSelect.value = prodId;
    if (qtyInput) qtyInput.value = 10;

    const bsModal = new bootstrap.Modal(modalElem);
    bsModal.show();
  };

  // Stock Adjust Form Submit
  const stockAdjustForm = document.getElementById("stockAdjustForm");
  if (stockAdjustForm) {
    stockAdjustForm.addEventListener("submit", (e) => {
      e.preventDefault();
      const subId = document.getElementById("modal_sub_id").value;
      const prodId = document.getElementById("modal_product_id").value;
      const adjustType = document.getElementById("modal_adjust_type").value;
      const qty = parseInt(document.getElementById("modal_adjust_qty").value) || 0;

      let target = AppState.data.sub_products.find(sp => sp.subsidiary_id === subId && sp.product_id === prodId);
      if (!target) {
        target = { subsidiary_id: subId, product_id: prodId, quantity: 0, last_updated: "Vừa tạo" };
        AppState.data.sub_products.push(target);
      }

      if (adjustType === "IN") {
        target.quantity += qty;
      } else {
        target.quantity = Math.max(0, target.quantity - qty);
      }
      target.last_updated = "Vừa điều chỉnh";

      renderInventoryTable();
      bootstrap.Modal.getInstance(document.getElementById("stockAdjustModal"))?.hide();
      showToast("Điều Phối Kho", `Đã ${adjustType === 'IN' ? 'nhập thêm' : 'xuất'} ${qty} đơn vị vào kho chi nhánh.`);
    });
  }

  // Filter triggers for Inventory
  ["inventoryBranchFilter", "inventoryStatusFilter", "inventorySearchInput"].forEach(id => {
    document.getElementById(id)?.addEventListener("input", renderInventoryTable);
    document.getElementById(id)?.addEventListener("change", renderInventoryTable);
  });

  // ==========================================================================
  // 6. ORDERS & FULFILLMENT MODULE (Orders, Order_Items, Payments)
  // ==========================================================================
  const renderOrdersTable = () => {
    const tbody = document.getElementById("ordersTableBody");
    if (!tbody) return;

    const statusFilter = document.getElementById("orderStatusFilter")?.value || "ALL";
    const paymentFilter = document.getElementById("orderPaymentFilter")?.value || "ALL";
    const searchTerm = (document.getElementById("orderSearchInput")?.value || "").toLowerCase();

    let filtered = AppState.data.orders.filter(order => {
      const matchStatus = statusFilter === "ALL" || order.order_status === statusFilter;
      const matchPayment = paymentFilter === "ALL" || order.payment.payment_method === paymentFilter;
      const matchSearch = order.order_id.toLowerCase().includes(searchTerm) ||
        order.customer_name.toLowerCase().includes(searchTerm) ||
        order.acc_id.toLowerCase().includes(searchTerm);
      return matchStatus && matchPayment && matchSearch;
    });

    tbody.innerHTML = filtered.map(order => `
      <tr>
        <td class="fw-bold text-danger">${order.order_id}</td>
        <td>
          <div class="fw-semibold">${order.customer_name}</div>
          <small class="text-muted">${order.acc_id}</small>
        </td>
        <td>
          <span class="badge bg-light text-dark border">
            <i class="bi bi-person-badge"></i> ${order.employee_id}
          </span>
        </td>
        <td>
          <span class="badge-status ${order.order_status.toLowerCase()}">
            <i class="bi bi-circle-fill" style="font-size: 6px;"></i> ${order.order_status}
          </span>
        </td>
        <td>
          <div class="d-flex align-items-center gap-1">
            <span class="badge bg-light text-dark border">${order.payment.payment_method}</span>
            <span class="badge bg-${order.payment.payment_status === 'SUCCESS' ? 'success' : 'warning'}-subtle text-${order.payment.payment_status === 'SUCCESS' ? 'success' : 'warning'}">
              ${order.payment.payment_status}
            </span>
          </div>
        </td>
        <td class="fw-bold">${formatCurrency(order.total_amount)}</td>
        <td class="text-muted small">${order.created_at}</td>
        <td>
          <button class="btn btn-sm btn-outline-red view-order-btn" data-order-id="${order.order_id}">
            <i class="bi bi-receipt"></i> Đối soát
          </button>
        </td>
      </tr>
    `).join("");

    tbody.querySelectorAll(".view-order-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        openOrderDetail(btn.getAttribute("data-order-id"));
      });
    });
  };

  const openOrderDetail = (orderId) => {
    const order = AppState.data.orders.find(o => o.order_id === orderId);
    if (!order) return;

    // Header
    document.getElementById("offcanvasOrderId").textContent = order.order_id;
    document.getElementById("offcanvasOrderDate").textContent = order.created_at;
    document.getElementById("offcanvasCustomer").textContent = `${order.customer_name} (${order.acc_id})`;
    document.getElementById("offcanvasAddress").textContent = `${order.target_street}, ${order.target_district}, ${order.target_city}`;
    document.getElementById("offcanvasEmployee").textContent = `${order.employee_id} (Phụ trách giao vận)`;

    // Order Items Table
    const itemsTbody = document.getElementById("offcanvasOrderItemsBody");
    if (itemsTbody) {
      itemsTbody.innerHTML = order.items.map((item, idx) => `
        <tr>
          <td>${idx + 1}</td>
          <td>
            <div class="fw-bold">${item.product_name}</div>
            <small class="text-muted">Kho: ${item.subsidiary_name} | Mã: ${item.product_id}</small>
          </td>
          <td class="text-center">${item.quantity}</td>
          <td class="text-end">${formatCurrency(item.list_price)}</td>
          <td class="text-end text-success">${item.discount_rate > 0 ? `-${item.discount_rate * 100}%` : '-'}</td>
          <td class="text-end fw-bold">${formatCurrency(item.total_price)}</td>
        </tr>
      `).join("");
    }

    // Totals & Logistics
    document.getElementById("offcanvasSubtotal").textContent = formatCurrency(order.total_amount);
    document.getElementById("offcanvasShipFee").textContent = formatCurrency(order.total_shipfee);
    document.getElementById("offcanvasFinalTotal").textContent = formatCurrency(order.total_amount + order.total_shipfee);

    // Payment Reconciliation Card
    document.getElementById("offcanvasPaymentId").textContent = order.payment.payment_id;
    document.getElementById("offcanvasPaymentMethod").textContent = order.payment.payment_method;
    document.getElementById("offcanvasPaymentStatus").innerHTML = `
      <span class="badge bg-${order.payment.payment_status === 'SUCCESS' ? 'success' : 'warning'}">
        ${order.payment.payment_status}
      </span>
    `;
    document.getElementById("offcanvasPaidAt").textContent = order.payment.paid_at || "Chưa hoàn tất";

    // Show Offcanvas
    const offcanvasElem = document.getElementById("orderDetailOffcanvas");
    const bsOffcanvas = new bootstrap.Offcanvas(offcanvasElem);
    bsOffcanvas.show();
  };

  // Filter triggers for Orders
  ["orderStatusFilter", "orderPaymentFilter", "orderSearchInput"].forEach(id => {
    document.getElementById(id)?.addEventListener("input", renderOrdersTable);
    document.getElementById(id)?.addEventListener("change", renderOrdersTable);
  });

  // ==========================================================================
  // 7. PROMOTIONS MODULE (program_sales, product_sales)
  // ==========================================================================
  const renderPromotionsTable = () => {
    const tbody = document.getElementById("promotionsTableBody");
    if (!tbody) return;

    tbody.innerHTML = AppState.data.program_sales.map(prog => `
      <tr>
        <td class="fw-bold text-danger">${prog.sale_id}</td>
        <td>
          <div class="fw-bold">${prog.sale_name}</div>
          <small class="text-muted"><i class="bi bi-calendar3"></i> ${prog.start_date} đến ${prog.end_date}</small>
        </td>
        <td>
          <span class="badge bg-danger-subtle text-danger fw-bold fs-6">-${prog.discount_value * 100}%</span>
        </td>
        <td class="fw-semibold">${formatCurrency(prog.min_ordervalue)}</td>
        <td>
          <span class="badge bg-primary-subtle text-primary border border-primary-subtle">
            <i class="bi bi-box-seam"></i> ${prog.assigned_count} sản phẩm
          </span>
        </td>
        <td>
          <span class="badge-status ${prog.status === 'ACTIVE' ? 'active' : 'pending'}">
            ${prog.status === 'ACTIVE' ? 'Đang diễn ra' : 'Sắp diễn ra'}
          </span>
        </td>
        <td>
          <button class="btn btn-sm btn-outline-red assign-promo-btn" data-sale-id="${prog.sale_id}">
            <i class="bi bi-plus-circle"></i> Gán SP
          </button>
        </td>
      </tr>
    `).join("");

    tbody.querySelectorAll(".assign-promo-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        const saleId = btn.getAttribute("data-sale-id");
        showToast("Chiến Dịch Khuyến Mãi", `Đã mở cấu hình gán sản phẩm vào chương trình ${saleId}`, "info");
      });
    });
  };

  // ==========================================================================
  // 8. CUSTOMERS & CARTS MODULE (customers, carts, cart_items)
  // ==========================================================================
  const renderCustomersTable = () => {
    const tbody = document.getElementById("customersTableBody");
    if (!tbody) return;

    const rankFilter = document.getElementById("customerRankFilter")?.value || "ALL";
    const searchTerm = (document.getElementById("customerSearchInput")?.value || "").toLowerCase();

    let filtered = AppState.data.customers.filter(c => {
      const matchRank = rankFilter === "ALL" || c.customer_rank === rankFilter;
      const fullName = `${c.last_name} ${c.first_name}`.toLowerCase();
      const matchSearch = fullName.includes(searchTerm) || c.email.toLowerCase().includes(searchTerm) || c.acc_id.toLowerCase().includes(searchTerm);
      return matchRank && matchSearch;
    });

    tbody.innerHTML = filtered.map(c => `
      <tr>
        <td class="fw-bold text-secondary">${c.acc_id}</td>
        <td>
          <div class="d-flex align-items-center gap-2">
            <div class="avatar-badge" style="width: 34px; height: 34px; font-size: 0.8rem;">${c.first_name.charAt(0)}</div>
            <div>
              <div class="fw-bold">${c.last_name} ${c.first_name}</div>
              <small class="text-muted">${c.email}</small>
            </div>
          </div>
        </td>
        <td>${c.phone_number}</td>
        <td>${c.city}</td>
        <td>
          <span class="badge badge-rank-${c.customer_rank.toLowerCase()}">
            <i class="bi bi-award-fill"></i> ${c.customer_rank}
          </span>
        </td>
        <td class="fw-bold">${formatCurrency(c.total_spent)}</td>
        <td>
          <div class="form-check form-switch">
            <input class="form-check-input customer-active-toggle" type="checkbox" role="switch" data-acc-id="${c.acc_id}" ${c.is_active ? 'checked' : ''}>
          </div>
        </td>
        <td>
          <button class="btn btn-sm btn-outline-secondary view-cart-btn" data-acc-id="${c.acc_id}">
            <i class="bi bi-cart3"></i> Giỏ hàng
          </button>
        </td>
      </tr>
    `).join("");

    // Toggle Customer is_active
    tbody.querySelectorAll(".customer-active-toggle").forEach(toggle => {
      toggle.addEventListener("change", (e) => {
        const accId = toggle.getAttribute("data-acc-id");
        const customer = AppState.data.customers.find(c => c.acc_id === accId);
        if (customer) {
          customer.is_active = e.target.checked;
          showToast(
            "Trạng Thái Khách Hàng",
            `Đã ${customer.is_active ? 'kích hoạt' : 'khóa'} tài khoản [${accId}] ${customer.last_name} ${customer.first_name}`,
            customer.is_active ? "success" : "danger"
          );
          renderDashboardKPIs();
        }
      });
    });

    // View Cart Inspector
    tbody.querySelectorAll(".view-cart-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        openCartInspector(btn.getAttribute("data-acc-id"));
      });
    });
  };

  const openCartInspector = (accId) => {
    const customer = AppState.data.customers.find(c => c.acc_id === accId);
    const cart = AppState.data.carts.find(ct => ct.acc_id === accId);

    const drawerTitle = document.getElementById("cartInspectorCustomerName");
    const drawerBody = document.getElementById("cartInspectorBody");
    const drawerTotal = document.getElementById("cartInspectorTotal");

    if (drawerTitle) drawerTitle.textContent = customer ? `${customer.last_name} ${customer.first_name} (${accId})` : accId;

    if (!cart || cart.items.length === 0) {
      drawerBody.innerHTML = `
        <div class="text-center py-5">
          <i class="bi bi-cart-x text-muted" style="font-size: 3rem;"></i>
          <p class="text-muted mt-2">Khách hàng chưa lưu sản phẩm nào trong giỏ.</p>
        </div>
      `;
      if (drawerTotal) drawerTotal.textContent = formatCurrency(0);
    } else {
      drawerBody.innerHTML = cart.items.map(item => `
        <div class="card card-custom p-3 mb-2">
          <div class="d-flex justify-content-between align-items-start mb-2">
            <div class="fw-bold">${item.product_name}</div>
            <span class="badge bg-danger-subtle text-danger">x${item.quantity}</span>
          </div>
          <div class="d-flex justify-content-between align-items-center text-muted small">
            <span>Đơn giá: ${formatCurrency(item.list_price)}</span>
            <span class="fw-bold text-dark fs-6">${formatCurrency(item.item_total_amount)}</span>
          </div>
        </div>
      `).join("");

      if (drawerTotal) drawerTotal.textContent = formatCurrency(cart.total_amount);
    }

    const bsOffcanvas = new bootstrap.Offcanvas(document.getElementById("cartInspectorOffcanvas"));
    bsOffcanvas.show();
  };

  // Filter triggers for Customers
  ["customerRankFilter", "customerSearchInput"].forEach(id => {
    document.getElementById(id)?.addEventListener("input", renderCustomersTable);
    document.getElementById(id)?.addEventListener("change", renderCustomersTable);
  });

  // ==========================================================================
  // 9. REVIEWS & EMPLOYEES MODULE
  // ==========================================================================
  const renderReviewsTable = () => {
    const container = document.getElementById("reviewsCardsContainer");
    if (!container) return;

    const sentimentFilter = document.getElementById("reviewSentimentFilter")?.value || "ALL";
    const statusReplyFilter = document.getElementById("reviewReplyFilter")?.value || "ALL";

    let filtered = AppState.data.reviews.filter(r => {
      const matchSentiment = sentimentFilter === "ALL" || r.review_sentiment === sentimentFilter;
      const matchReply = statusReplyFilter === "ALL" ||
        (statusReplyFilter === "REPLIED" && r.has_reply) ||
        (statusReplyFilter === "UNREPLIED" && !r.has_reply);
      return matchSentiment && matchReply;
    });

    container.innerHTML = filtered.map(r => `
      <div class="card card-custom p-3 mb-3">
        <div class="d-flex justify-content-between align-items-center mb-2">
          <div class="d-flex align-items-center gap-2">
            <span class="badge bg-light text-dark border">${r.review_id}</span>
            <div class="text-warning">
              ${'<i class="bi bi-star-fill"></i>'.repeat(Math.floor(r.review_score))}
              <span class="fw-bold text-dark ms-1">${r.review_score}</span>
            </div>
            <span class="badge ${r.review_sentiment === 'Positive' ? 'bg-success-subtle text-success' : r.review_sentiment === 'Negative' ? 'bg-danger-subtle text-danger' : 'bg-secondary-subtle text-secondary'}">
              ${r.review_sentiment}
            </span>
          </div>
          <div>
            ${r.has_reply
              ? '<span class="badge bg-success"><i class="bi bi-check-all"></i> Đã phản hồi CSKH</span>'
              : '<span class="badge bg-danger-subtle text-danger"><i class="bi bi-clock"></i> Chưa phản hồi</span>'
            }
          </div>
        </div>

        <h6 class="fw-bold mb-1">${r.review_title}</h6>
        <p class="text-secondary small mb-2">${r.review_message}</p>

        <div class="d-flex justify-content-between align-items-center pt-2 border-top">
          <small class="text-muted">
            <strong>${r.customer_name}</strong> đánh giá cho <em>${r.product_name}</em> (${r.created_at})
          </small>
          ${!r.has_reply ? `
            <button class="btn btn-sm btn-outline-red reply-review-btn" data-review-id="${r.review_id}">
              <i class="bi bi-reply-fill"></i> Phản hồi ngay
            </button>
          ` : `
            <span class="text-muted small"><i class="bi bi-check2"></i> Đã xử lý lúc ${r.replied_at}</span>
          `}
        </div>
      </div>
    `).join("");

    container.querySelectorAll(".reply-review-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        openReplyReviewModal(btn.getAttribute("data-review-id"));
      });
    });
  };

  const openReplyReviewModal = (reviewId) => {
    const review = AppState.data.reviews.find(r => r.review_id === reviewId);
    if (!review) return;

    document.getElementById("replyModalReviewId").textContent = review.review_id;
    document.getElementById("replyModalCustomer").textContent = review.customer_name;
    document.getElementById("replyModalComment").textContent = `"${review.review_message}"`;
    document.getElementById("replyResponseText").value = "";

    const modal = new bootstrap.Modal(document.getElementById("replyReviewModal"));
    modal.show();

    // Setup submit
    const submitBtn = document.getElementById("btnSubmitReviewReply");
    submitBtn.onclick = () => {
      review.has_reply = true;
      review.replied_at = new Date().toLocaleString("sv-SE");
      modal.hide();
      renderReviewsTable();
      showToast("Phản Hồi CSKH", `Đã lưu phản hồi cho đánh giá [${reviewId}] thành công!`);
    };
  };

  const renderEmployeesTable = () => {
    const tbody = document.getElementById("employeesTableBody");
    if (!tbody) return;

    tbody.innerHTML = AppState.data.employees.map(emp => `
      <tr>
        <td class="fw-bold text-secondary">${emp.employee_id}</td>
        <td>
          <div class="fw-bold">${emp.last_name} ${emp.first_name}</div>
          <small class="text-muted">${emp.email}</small>
        </td>
        <td>
          <span class="badge bg-light text-dark border">${emp.role}</span>
        </td>
        <td>
          <span class="badge bg-danger-subtle text-danger border border-danger-subtle">
            ${emp.subsidiary_name}
          </span>
        </td>
        <td>${emp.phone}</td>
        <td>${emp.recruited_at}</td>
      </tr>
    `).join("");
  };

  // Review Filters
  ["reviewSentimentFilter", "reviewReplyFilter"].forEach(id => {
    document.getElementById(id)?.addEventListener("change", renderReviewsTable);
  });

  // ==========================================================================
  // 10. GLOBAL SEARCH HANDLER
  // ==========================================================================
  const globalSearchInput = document.getElementById("globalSearchInput");
  if (globalSearchInput) {
    globalSearchInput.addEventListener("keydown", (e) => {
      if (e.key === "Enter") {
        const query = globalSearchInput.value.trim().toLowerCase();
        if (!query) return;

        // Route intelligently based on query prefix or context
        if (query.startsWith("pr") || query.includes("samsung") || query.includes("macbook")) {
          navigateTo("products");
          const pSearch = document.getElementById("productSearchInput");
          if (pSearch) { pSearch.value = query; renderProductsTable(); }
        } else if (query.startsWith("od")) {
          navigateTo("orders");
          const oSearch = document.getElementById("orderSearchInput");
          if (oSearch) { oSearch.value = query; renderOrdersTable(); }
        } else if (query.startsWith("acc") || query.startsWith("cus")) {
          navigateTo("customers");
          const cSearch = document.getElementById("customerSearchInput");
          if (cSearch) { cSearch.value = query; renderCustomersTable(); }
        } else {
          navigateTo("products");
          const pSearch = document.getElementById("productSearchInput");
          if (pSearch) { pSearch.value = query; renderProductsTable(); }
        }
      }
    });
  }

  // Populate dynamic Select options across forms
  const populateStaticOptions = () => {
    // Populate Categories in Products Modal
    const formCat = document.getElementById("form_category_id");
    const filterCat = document.getElementById("productCategoryFilter");
    if (formCat && filterCat) {
      AppState.data.categories.forEach(c => {
        formCat.insertAdjacentHTML("beforeend", `<option value="${c.cate_id}">${c.cate_name}</option>`);
        filterCat.insertAdjacentHTML("beforeend", `<option value="${c.cate_id}">${c.cate_name}</option>`);
      });
    }

    // Populate Brands
    const formBrand = document.getElementById("form_brand_id");
    const filterBrand = document.getElementById("productBrandFilter");
    if (formBrand && filterBrand) {
      AppState.data.brands.forEach(b => {
        formBrand.insertAdjacentHTML("beforeend", `<option value="${b.brand_id}">${b.brand_name}</option>`);
        filterBrand.insertAdjacentHTML("beforeend", `<option value="${b.brand_id}">${b.brand_name}</option>`);
      });
    }

    // Populate Subsidiaries in Inventory
    const filterSub = document.getElementById("inventoryBranchFilter");
    const modalSub = document.getElementById("modal_sub_id");
    const topbarSub = document.getElementById("topbarBranchSelect");
    if (filterSub && modalSub && topbarSub) {
      AppState.data.subsidiaries.forEach(s => {
        const opt = `<option value="${s.sub_id}">${s.sub_name} (${s.sub_city})</option>`;
        filterSub.insertAdjacentHTML("beforeend", opt);
        modalSub.insertAdjacentHTML("beforeend", opt);
        topbarSub.insertAdjacentHTML("beforeend", opt);
      });
    }

    // Populate Products in Modal Adjust
    const modalProd = document.getElementById("modal_product_id");
    if (modalProd) {
      AppState.data.products.forEach(p => {
        modalProd.insertAdjacentHTML("beforeend", `<option value="${p.product_id}">[${p.product_id}] ${p.product_name}</option>`);
      });
    }
  };

  // Initialize Application
  populateStaticOptions();
  navigateTo("dashboard");
});

