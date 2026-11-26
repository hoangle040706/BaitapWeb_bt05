<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
  <title>Tổng quan</title>
</head>
<body>
  <div class="container-main">
    <h1 class="page-heading" style="margin-bottom:0.5rem;">Quản trị hệ thống</h1>
    <p class="page-subtitle">
      Xin chào <strong style="color:var(--stone-900);"><c:out value="${sessionScope.currentUser.displayName}"/></strong>
      (<code><c:out value="${sessionScope.currentUser.role}"/></code>).
    </p>

    <div class="admin-tabs">
      <a href="<c:url value='/admin/home'/>" class="admin-tab active">Tổng quan</a>
      <span class="admin-tab" title="Hoàn thiện ở mốc 30/11/2026">Danh mục (${categoryCount})</span>
      <span class="admin-tab" title="Hoàn thiện ở mốc 30/11/2026">Người dùng (${userCount})</span>
    </div>

    <div class="stat-grid">
      <div class="panel-card">
        <p class="card-meta" style="margin:0;">Tổng số danh mục</p>
        <p class="stat-value">${categoryCount}</p>
        <p class="card-meta">Sẵn sàng cho CRUD + Tìm kiếm &amp; Phân trang</p>
      </div>
      <div class="panel-card">
        <p class="card-meta" style="margin:0;">Tổng số tài khoản</p>
        <p class="stat-value">${userCount}</p>
        <p class="card-meta">Đã mã hóa mật khẩu bằng BCrypt</p>
      </div>
      <div class="panel-card">
        <p class="card-meta" style="margin:0;">Phiên đăng nhập hiện tại</p>
        <p class="stat-value" style="font-size:1.5rem;margin-top:0.85rem;">
          <c:out value="${sessionScope.currentUser.username}"/>
        </p>
        <p class="card-meta"><c:out value="${sessionScope.currentUser.email}"/></p>
      </div>
    </div>
  </div>
</body>
</html>
