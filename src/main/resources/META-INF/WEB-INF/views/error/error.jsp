<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
  <title>Thông báo lỗi</title>
</head>
<body>
  <div class="auth-container" style="max-width:34rem;">
    <div class="panel-card" style="text-align:center;">
      <p class="hero-eyebrow" style="margin-bottom:0.5rem;">Mã trạng thái ${statusCode != null ? statusCode : 500}</p>
      <h1 class="page-heading" style="font-size:2rem;">
        <c:out value="${not empty errorTitle ? errorTitle : 'Đã xảy ra lỗi'}"/>
      </h1>
      <p class="page-subtitle" style="margin-top:0.75rem;">
        <c:out value="${not empty errorMessage ? errorMessage : 'Vui lòng kiểm tra lại thao tác của bạn.'}"/>
      </p>
      <div style="display:flex;justify-content:center;gap:0.75rem;flex-wrap:wrap;">
        <a href="<c:url value='/'/>" class="btn-primary-pill">Về trang chủ</a>
        <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.role == 'ADMIN'}">
          <a href="<c:url value='/admin/home'/>" class="btn-outline-pill">Về trang quản trị</a>
        </c:if>
      </div>
    </div>
  </div>
</body>
</html>
