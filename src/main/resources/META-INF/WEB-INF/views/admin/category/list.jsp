<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
  <title>Danh sách danh mục</title>
</head>
<body>
  <div class="container-main">
    <div class="section-header" style="border-bottom:none;margin-bottom:1rem;padding-bottom:0;">
      <div>
        <h1 class="page-heading">Quản lý danh mục</h1>
        <p class="page-subtitle" style="margin-bottom:0;">Tìm kiếm và phân trang danh mục (Spring Data JPA Pageable).</p>
      </div>
      <a href="<c:url value='/admin/categories/add'/>" class="btn-amber-pill">+ Thêm danh mục</a>
    </div>

    <div class="admin-tabs">
      <a href="<c:url value='/admin/home'/>" class="admin-tab">Tổng quan</a>
      <a href="<c:url value='/admin/categories/searchpaginated'/>" class="admin-tab active">Danh mục (${categoryPage.totalElements})</a>
      <a href="<c:url value='/admin/users/searchpaginated'/>" class="admin-tab">Người dùng</a>
    </div>

    <c:if test="${not empty message}">
      <div class="alert alert-success" role="status"><c:out value="${message}"/></div>
    </c:if>

    <div class="toolbar-row">
      <form action="<c:url value='/admin/categories/searchpaginated'/>" method="get" class="search-form">
        <input type="text"
               name="name"
               id="name"
               value="<c:out value='${name}'/>"
               placeholder="Nhập từ khóa để tìm..."
               class="search-input"/>
        <input type="hidden" name="size" value="${categoryPage.size}"/>
        <button type="submit" class="btn-primary-pill">Tìm kiếm</button>
        <c:if test="${not empty name}">
          <a href="<c:url value='/admin/categories/searchpaginated?size=${categoryPage.size}'/>" class="btn-outline-pill">Xóa lọc</a>
        </c:if>
      </form>

      <form action="<c:url value='/admin/categories/searchpaginated'/>" method="get" style="display:flex;align-items:center;gap:0.5rem;">
        <c:if test="${not empty name}">
          <input type="hidden" name="name" value="<c:out value='${name}'/>"/>
        </c:if>
        <label for="size" style="font-size:0.875rem;color:var(--stone-600);">Hiển thị:</label>
        <select name="size" id="size" class="size-select" onchange="this.form.submit()">
          <option ${categoryPage.size == 3 ? 'selected' : ''} value="3">3</option>
          <option ${categoryPage.size == 5 ? 'selected' : ''} value="5">5</option>
          <option ${categoryPage.size == 10 ? 'selected' : ''} value="10">10</option>
          <option ${categoryPage.size == 15 ? 'selected' : ''} value="15">15</option>
          <option ${categoryPage.size == 20 ? 'selected' : ''} value="20">20</option>
        </select>
      </form>
    </div>

    <c:choose>
      <c:when test="${!categoryPage.hasContent()}">
        <div class="empty-box">Không tìm thấy danh mục nào phù hợp.</div>
      </c:when>
      <c:otherwise>
        <div class="table-card">
          <table class="data-table">
            <thead>
              <tr>
                <th>STT</th>
                <th>Hình ảnh</th>
                <th>Tên danh mục</th>
                <th>Trạng thái</th>
                <th style="text-align:right;">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${categoryPage.content}" var="cate" varStatus="STT">
                <tr>
                  <td>${categoryPage.number * categoryPage.size + STT.index + 1}</td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty cate.images}">
                        <code><c:out value="${cate.images}"/></code>
                      </c:when>
                      <c:otherwise>
                        <span style="color:var(--stone-500);">—</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td><strong><c:out value="${cate.name}"/></strong></td>
                  <td>
                    <c:choose>
                      <c:when test="${cate.status == 1}">
                        <span class="badge-pill badge-active">Hoạt động</span>
                      </c:when>
                      <c:otherwise>
                        <span class="badge-pill badge-inactive">Tạm ẩn</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td style="text-align:right;">
                    <div class="action-group">
                      <a href="<c:url value='/admin/categories/edit/${cate.id}'/>" class="btn-table-edit">Sửa</a>
                      <form action="<c:url value='/admin/categories/delete/${cate.id}'/>" method="post" style="margin:0;">
                        <button type="submit"
                                class="btn-table-delete"
                                onclick="return confirm('Xóa danh mục này?');">
                          Xóa
                        </button>
                      </form>
                    </div>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </c:otherwise>
    </c:choose>

    <div class="pagination-bar">
      <p class="section-count">
        Trang <strong>${categoryPage.totalPages == 0 ? 0 : categoryPage.number + 1}</strong> / ${categoryPage.totalPages}
        (Tổng ${categoryPage.totalElements} danh mục)
      </p>

      <c:if test="${categoryPage.totalPages > 1}">
        <ul class="pagination-list">
          <c:if test="${categoryPage.hasPrevious()}">
            <c:url var="prevUrl" value="/admin/categories/searchpaginated">
              <c:if test="${not empty name}"><c:param name="name" value="${name}"/></c:if>
              <c:param name="size" value="${categoryPage.size}"/>
              <c:param name="page" value="${categoryPage.number}"/>
            </c:url>
            <li class="page-item"><a href="${prevUrl}">&laquo;</a></li>
          </c:if>

          <c:forEach items="${pageNumbers}" var="pageNumber">
            <c:url var="pageUrl" value="/admin/categories/searchpaginated">
              <c:if test="${not empty name}"><c:param name="name" value="${name}"/></c:if>
              <c:param name="size" value="${categoryPage.size}"/>
              <c:param name="page" value="${pageNumber}"/>
            </c:url>
            <li class="${pageNumber == categoryPage.number + 1 ? 'page-item active' : 'page-item'}">
              <a href="${pageUrl}">${pageNumber}</a>
            </li>
          </c:forEach>

          <c:if test="${categoryPage.hasNext()}">
            <c:url var="nextUrl" value="/admin/categories/searchpaginated">
              <c:if test="${not empty name}"><c:param name="name" value="${name}"/></c:if>
              <c:param name="size" value="${categoryPage.size}"/>
              <c:param name="page" value="${categoryPage.number + 2}"/>
            </c:url>
            <li class="page-item"><a href="${nextUrl}">&raquo;</a></li>
          </c:if>
        </ul>
      </c:if>
    </div>
  </div>
</body>
</html>
