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
      <a href="<c:url value='/admin/categories/searchpaginated'/>" class="admin-tab">Danh mục (${categoryCount})</a>
      <a href="<c:url value='/admin/users/searchpaginated'/>" class="admin-tab">Người dùng (${userCount})</a>
    </div>

    <div class="stat-grid">
      <div class="panel-card">
        <p class="card-meta" style="margin:0;">Tổng số danh mục</p>
        <p class="stat-value">${categoryCount}</p>
        <p class="card-meta" style="margin-bottom:1rem;">CRUD đầy đủ với tìm kiếm &amp; phân trang</p>
        <a href="<c:url value='/admin/categories/searchpaginated'/>" class="btn-primary-pill">Quản lý danh mục</a>
      </div>
      <div class="panel-card">
        <p class="card-meta" style="margin:0;">Tổng số tài khoản</p>
        <p class="stat-value">${userCount}</p>
        <p class="card-meta" style="margin-bottom:1rem;">CRUD, lọc theo vai trò/trạng thái &amp; BCrypt</p>
        <a href="<c:url value='/admin/users/searchpaginated'/>" class="btn-primary-pill">Quản lý người dùng</a>
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
