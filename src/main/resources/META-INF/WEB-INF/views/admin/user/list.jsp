<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
  <title>Danh sách người dùng</title>
</head>
<body>
  <div class="container-main">
    <div class="section-header" style="border-bottom:none;margin-bottom:1rem;padding-bottom:0;">
      <div>
        <h1 class="page-heading">Quản lý người dùng</h1>
        <p class="page-subtitle" style="margin-bottom:0;">Tìm kiếm, lọc vai trò/trạng thái và phân trang (Spring Data JPA Specification).</p>
      </div>
      <a href="<c:url value='/admin/users/add'/>" class="btn-amber-pill">+ Thêm người dùng</a>
    </div>

    <div class="admin-tabs">
      <a href="<c:url value='/admin/home'/>" class="admin-tab">Tổng quan</a>
      <a href="<c:url value='/admin/categories/searchpaginated'/>" class="admin-tab">Danh mục</a>
      <a href="<c:url value='/admin/users/searchpaginated'/>" class="admin-tab active">Người dùng (${userPage.totalElements})</a>
    </div>

    <c:if test="${not empty message}">
      <div class="alert alert-success" role="status"><c:out value="${message}"/></div>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-error" role="alert"><c:out value="${errorMessage}"/></div>
    </c:if>

    <div class="toolbar-row">
      <form action="<c:url value='/admin/users/searchpaginated'/>" method="get" class="search-form">
        <input type="text"
               name="keyword"
               id="keyword"
               value="<c:out value='${keyword}'/>"
               placeholder="Tìm theo username, họ tên hoặc email..."
               class="search-input"/>

        <select name="role" class="size-select" aria-label="Lọc theo vai trò">
          <option value="">Tất cả vai trò</option>
          <c:forEach items="${roles}" var="r">
            <option value="${r}" ${selectedRole == r ? 'selected' : ''}>${r}</option>
          </c:forEach>
        </select>

        <select name="status" class="size-select" aria-label="Lọc theo trạng thái">
          <option value="">Tất cả trạng thái</option>
          <option value="1" ${selectedStatus == 1 ? 'selected' : ''}>Hoạt động</option>
          <option value="0" ${selectedStatus == 0 ? 'selected' : ''}>Tạm khóa</option>
        </select>

        <input type="hidden" name="size" value="${userPage.size}"/>
        <button type="submit" class="btn-primary-pill">Tìm kiếm</button>
        <c:if test="${not empty keyword or not empty selectedRole or selectedStatus != null}">
          <a href="<c:url value='/admin/users/searchpaginated?size=${userPage.size}'/>" class="btn-outline-pill">Xóa lọc</a>
        </c:if>
      </form>

      <form action="<c:url value='/admin/users/searchpaginated'/>" method="get" style="display:flex;align-items:center;gap:0.5rem;">
        <c:if test="${not empty keyword}">
          <input type="hidden" name="keyword" value="<c:out value='${keyword}'/>"/>
        </c:if>
        <c:if test="${not empty selectedRole}">
          <input type="hidden" name="role" value="${selectedRole}"/>
        </c:if>
        <c:if test="${selectedStatus != null}">
          <input type="hidden" name="status" value="${selectedStatus}"/>
        </c:if>
        <label for="size" style="font-size:0.875rem;color:var(--stone-600);">Hiển thị:</label>
        <select name="size" id="size" class="size-select" onchange="this.form.submit()">
          <option ${userPage.size == 3 ? 'selected' : ''} value="3">3</option>
          <option ${userPage.size == 5 ? 'selected' : ''} value="5">5</option>
          <option ${userPage.size == 10 ? 'selected' : ''} value="10">10</option>
          <option ${userPage.size == 15 ? 'selected' : ''} value="15">15</option>
          <option ${userPage.size == 20 ? 'selected' : ''} value="20">20</option>
        </select>
      </form>
    </div>

    <c:choose>
      <c:when test="${!userPage.hasContent()}">
        <div class="empty-box">Không tìm thấy người dùng nào phù hợp.</div>
      </c:when>
      <c:otherwise>
        <div class="table-card">
          <table class="data-table">
            <thead>
              <tr>
                <th>STT</th>
                <th>Tài khoản</th>
                <th>Họ và tên</th>
                <th>Email</th>
                <th>Vai trò</th>
                <th>Trạng thái</th>
                <th style="text-align:right;">Thao tác</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach items="${userPage.content}" var="u" varStatus="STT">
                <tr>
                  <td>${userPage.number * userPage.size + STT.index + 1}</td>
                  <td>
                    <strong><c:out value="${u.username}"/></strong>
                    <c:if test="${u.id == sessionScope.currentUser.id}">
                      <span class="badge-pill badge-admin" style="margin-left:0.35rem;">Bạn</span>
                    </c:if>
                    <c:if test="${not empty u.phone}">
                      <div style="font-size:0.8rem;color:var(--stone-500);"><c:out value="${u.phone}"/></div>
                    </c:if>
                  </td>
                  <td><c:out value="${u.fullname}"/></td>
                  <td><c:out value="${u.email}"/></td>
                  <td>
                    <span class="badge-pill ${u.role == 'ADMIN' ? 'badge-admin' : 'badge-user'}">
                      <c:out value="${u.role}"/>
                    </span>
                  </td>
                  <td>
                    <c:choose>
                      <c:when test="${u.status == 1}">
                        <span class="badge-pill badge-active">Hoạt động</span>
                      </c:when>
                      <c:otherwise>
                        <span class="badge-pill badge-inactive">Tạm khóa</span>
                      </c:otherwise>
                    </c:choose>
                  </td>
                  <td style="text-align:right;">
                    <div class="action-group">
                      <a href="<c:url value='/admin/users/edit/${u.id}'/>" class="btn-table-edit">Sửa</a>
                      <c:if test="${u.id != sessionScope.currentUser.id}">
                        <form action="<c:url value='/admin/users/delete/${u.id}'/>" method="post" style="margin:0;">
                          <button type="submit"
                                  class="btn-table-delete"
                                  onclick="return confirm('Xóa tài khoản này?');">
                            Xóa
                          </button>
                        </form>
                      </c:if>
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
        Trang <strong>${userPage.totalPages == 0 ? 0 : userPage.number + 1}</strong> / ${userPage.totalPages}
        (Tổng ${userPage.totalElements} tài khoản)
      </p>

      <c:if test="${userPage.totalPages > 1}">
        <ul class="pagination-list">
          <c:if test="${userPage.hasPrevious()}">
            <c:url var="prevUrl" value="/admin/users/searchpaginated">
              <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
              <c:if test="${not empty selectedRole}"><c:param name="role" value="${selectedRole}"/></c:if>
              <c:if test="${selectedStatus != null}"><c:param name="status" value="${selectedStatus}"/></c:if>
              <c:param name="size" value="${userPage.size}"/>
              <c:param name="page" value="${userPage.number}"/>
            </c:url>
            <li class="page-item"><a href="${prevUrl}">&laquo;</a></li>
          </c:if>

          <c:forEach items="${pageNumbers}" var="pageNumber">
            <c:url var="pageUrl" value="/admin/users/searchpaginated">
              <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
              <c:if test="${not empty selectedRole}"><c:param name="role" value="${selectedRole}"/></c:if>
              <c:if test="${selectedStatus != null}"><c:param name="status" value="${selectedStatus}"/></c:if>
              <c:param name="size" value="${userPage.size}"/>
              <c:param name="page" value="${pageNumber}"/>
            </c:url>
            <li class="${pageNumber == userPage.number + 1 ? 'page-item active' : 'page-item'}">
              <a href="${pageUrl}">${pageNumber}</a>
            </li>
          </c:forEach>

          <c:if test="${userPage.hasNext()}">
            <c:url var="nextUrl" value="/admin/users/searchpaginated">
              <c:if test="${not empty keyword}"><c:param name="keyword" value="${keyword}"/></c:if>
              <c:if test="${not empty selectedRole}"><c:param name="role" value="${selectedRole}"/></c:if>
              <c:if test="${selectedStatus != null}"><c:param name="status" value="${selectedStatus}"/></c:if>
              <c:param name="size" value="${userPage.size}"/>
              <c:param name="page" value="${userPage.number + 2}"/>
            </c:url>
            <li class="page-item"><a href="${nextUrl}">&raquo;</a></li>
          </c:if>
        </ul>
      </c:if>
    </div>
  </div>
</body>
</html>
