<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<header class="site-header">
  <div class="header-inner">
    <a href="<c:url value='/'/>" class="brand">
      Rang <span class="brand-accent">Xay</span>
    </a>
    <nav class="site-nav">
      <a href="<c:url value='/'/>" class="nav-link">Trang chủ</a>
      <a href="<c:url value='/#danh-muc'/>" class="nav-link">Danh mục</a>

      <c:if test="${not empty sessionScope.currentUser and sessionScope.currentUser.role == 'ADMIN'}">
        <a href="<c:url value='/admin/home'/>" class="nav-pill-dark">Quản trị</a>
      </c:if>

      <c:choose>
        <c:when test="${not empty sessionScope.currentUser}">
          <div class="nav-group">
            <span class="nav-user-name">
              <c:out value="${sessionScope.currentUser.displayName}"/>
            </span>
            <form action="<c:url value='/logout'/>" method="post" style="margin:0;">
              <button type="submit" class="nav-logout-btn">Đăng xuất</button>
            </form>
          </div>
        </c:when>
        <c:otherwise>
          <div class="nav-group">
            <a href="<c:url value='/login'/>" class="nav-link">Đăng nhập</a>
            <a href="<c:url value='/register'/>" class="nav-pill-amber">Đăng ký</a>
          </div>
        </c:otherwise>
      </c:choose>
    </nav>
  </div>
</header>
