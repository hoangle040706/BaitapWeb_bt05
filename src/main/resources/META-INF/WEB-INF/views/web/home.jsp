<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
  <title>Rang Xay - Trang chủ</title>
</head>
<body>
  <c:if test="${not empty errorMessage}">
    <div class="section-wrap" style="padding-top:1.5rem;">
      <div class="alert alert-error" role="alert"><c:out value="${errorMessage}"/></div>
    </div>
  </c:if>

  <section class="hero-grid">
    <div>
      <p class="hero-eyebrow">Rang mới mỗi tuần</p>
      <h1 class="hero-title">Cà phê ngon bắt đầu từ hạt rang đúng độ.</h1>
      <p class="hero-desc">
        Hạt cà phê tuyển chọn từ Tây Nguyên, rang theo mẻ nhỏ và quản lý tập trung trên nền tảng Spring Boot 4 &amp; JSP/JSTL.
      </p>
      <div class="hero-actions">
        <a href="#danh-muc" class="btn-primary-pill">Xem danh mục</a>
        <c:choose>
          <c:when test="${empty sessionScope.currentUser}">
            <a href="<c:url value='/register'/>" class="btn-outline-pill">Tạo tài khoản</a>
          </c:when>
          <c:when test="${sessionScope.currentUser.role == 'ADMIN'}">
            <a href="<c:url value='/admin/categories/searchpaginated'/>" class="btn-outline-pill">Quản lý danh mục</a>
          </c:when>
          <c:otherwise>
            <span class="btn-outline-pill" style="cursor:default;">
              Xin chào, <c:out value="${sessionScope.currentUser.displayName}"/>
            </span>
          </c:otherwise>
        </c:choose>
      </div>
    </div>

    <div class="hero-showcase">
      <span class="hero-badge">Spring Boot 4 · JSP/JSTL · SiteMesh 3</span>
      <p class="hero-showcase-quote">
        “Mỗi mẻ rang là sự cân bằng giữa hương thơm tự nhiên, độ chua thanh và hậu vị ngọt kéo dài.”
      </p>
      <div class="hero-showcase-meta">
        <span>${categoryPage.totalElements} danh mục hoạt động</span>
        <span>
          <c:choose>
            <c:when test="${not empty sessionScope.currentUser}">
              Vai trò: <strong><c:out value="${sessionScope.currentUser.role}"/></strong>
            </c:when>
            <c:otherwise>Chưa đăng nhập</c:otherwise>
          </c:choose>
        </span>
      </div>
    </div>
  </section>

  <section id="danh-muc" class="section-wrap">
    <div class="section-header">
      <h2 class="section-title">Danh mục nổi bật</h2>
      <p class="section-count">${categoryPage.totalElements} danh mục</p>
    </div>

    <div class="toolbar-row">
      <form action="<c:url value='/#danh-muc'/>" method="get" class="search-form">
        <input type="text"
               name="name"
               value="<c:out value='${name}'/>"
               placeholder="Tìm kiếm danh mục..."
               class="search-input"/>
        <input type="hidden" name="size" value="${categoryPage.size}"/>
        <button type="submit" class="btn-primary-pill">Tìm kiếm</button>
        <c:if test="${not empty name}">
          <a href="<c:url value='/?size=${categoryPage.size}#danh-muc'/>" class="btn-outline-pill">Xóa lọc</a>
        </c:if>
      </form>

      <form action="<c:url value='/#danh-muc'/>" method="get" style="display:flex;align-items:center;gap:0.5rem;">
        <c:if test="${not empty name}">
          <input type="hidden" name="name" value="<c:out value='${name}'/>"/>
        </c:if>
        <label for="homeSize" style="font-size:0.875rem;color:var(--stone-600);">Hiển thị:</label>
        <select name="size" id="homeSize" class="size-select" onchange="this.form.submit()">
          <option ${categoryPage.size == 4 ? 'selected' : ''} value="4">4</option>
          <option ${categoryPage.size == 8 ? 'selected' : ''} value="8">8</option>
          <option ${categoryPage.size == 12 ? 'selected' : ''} value="12">12</option>
        </select>
      </form>
    </div>

    <c:choose>
      <c:when test="${!categoryPage.hasContent()}">
        <div class="empty-box">Không tìm thấy danh mục nào phù hợp.</div>
      </c:when>
      <c:otherwise>
        <div class="card-grid">
          <c:forEach items="${categoryPage.content}" var="cate" varStatus="st">
            <article class="card">
              <div class="card-thumb">${categoryPage.number * categoryPage.size + st.index + 1}</div>
              <h3 class="card-title"><c:out value="${cate.name}"/></h3>
              <p class="card-meta">Trạng thái: Đang hoạt động</p>
            </article>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>

    <c:if test="${categoryPage.totalPages > 1}">
      <div class="pagination-bar">
        <p class="section-count">
          Trang <strong>${categoryPage.number + 1}</strong> / ${categoryPage.totalPages}
        </p>
        <ul class="pagination-list">
          <c:forEach items="${pageNumbers}" var="pageNumber">
            <c:url var="homePageUrl" value="/">
              <c:if test="${not empty name}"><c:param name="name" value="${name}"/></c:if>
              <c:param name="size" value="${categoryPage.size}"/>
              <c:param name="page" value="${pageNumber}"/>
            </c:url>
            <li class="${pageNumber == categoryPage.number + 1 ? 'page-item active' : 'page-item'}">
              <a href="${homePageUrl}#danh-muc">${pageNumber}</a>
            </li>
          </c:forEach>
        </ul>
      </div>
    </c:if>
  </section>
</body>
</html>
