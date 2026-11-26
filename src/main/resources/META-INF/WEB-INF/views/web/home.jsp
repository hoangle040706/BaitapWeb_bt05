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
            <a href="<c:url value='/admin/home'/>" class="btn-outline-pill">Vào trang quản trị</a>
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
        <span>${categories.size()} danh mục hoạt động</span>
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
      <p class="section-count">${categories.size()} danh mục</p>
    </div>

    <c:choose>
      <c:when test="${empty categories}">
        <div class="empty-box">Chưa có danh mục nào đang hoạt động.</div>
      </c:when>
      <c:otherwise>
        <div class="card-grid">
          <c:forEach items="${categories}" var="cate" varStatus="st">
            <article class="card">
              <div class="card-thumb">${st.index + 1}</div>
              <h3 class="card-title"><c:out value="${cate.name}"/></h3>
              <p class="card-meta">Trạng thái: Đang hoạt động</p>
            </article>
          </c:forEach>
        </div>
      </c:otherwise>
    </c:choose>
  </section>
</body>
</html>
