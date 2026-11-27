<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<c:set var="reqUri" value="${not empty requestScope['jakarta.servlet.forward.request_uri'] ? requestScope['jakarta.servlet.forward.request_uri'] : pageContext.request.requestURI}" />
<header class="site-header">
  <div class="header-inner">
    <a href="<c:url value='/admin/home'/>" class="brand">
      Rang <span class="brand-accent">Xay</span> <span style="font-size:0.8rem;color:var(--stone-500);font-family:var(--font-sans);">Admin</span>
    </a>
    <nav class="site-nav">
      <a href="<c:url value='/'/>" class="nav-link">Trang chủ</a>
      <a href="<c:url value='/admin/home'/>" class="${reqUri == '/admin/home' or reqUri == '/admin' or reqUri == '/admin/' ? 'nav-pill-dark' : 'nav-link'}">Tổng quan</a>
      <a href="<c:url value='/admin/categories/searchpaginated'/>" class="${reqUri.startsWith('/admin/categories') ? 'nav-pill-dark' : 'nav-link'}">Danh mục</a>
      <a href="<c:url value='/admin/users/searchpaginated'/>" class="${reqUri.startsWith('/admin/users') ? 'nav-pill-dark' : 'nav-link'}">Người dùng</a>
      <div class="nav-group">
        <span class="nav-user-name">
          <c:out value="${sessionScope.currentUser.displayName}"/>
        </span>
        <form action="<c:url value='/logout'/>" method="post" style="margin:0;">
          <button type="submit" class="nav-logout-btn">Đăng xuất</button>
        </form>
      </div>
    </nav>
  </div>
</header>
