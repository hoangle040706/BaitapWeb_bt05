<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
  <title>${isEdit ? 'Sửa danh mục' : 'Thêm danh mục'}</title>
</head>
<body>
  <div class="auth-container" style="max-width:34rem;">
    <h1 class="page-heading">${isEdit ? 'Sửa danh mục' : 'Thêm danh mục'}</h1>
    <p class="page-subtitle">Cập nhật thông tin danh mục sản phẩm.</p>

    <c:if test="${not empty errorMessage}">
      <div class="alert alert-error" role="alert"><c:out value="${errorMessage}"/></div>
    </c:if>

    <div class="panel-card">
      <c:url var="saveUrl" value="/admin/categories/saveOrUpdate"/>
      <form:form action="${saveUrl}" method="post" modelAttribute="category" cssClass="form-stack">
        <form:hidden path="id"/>

        <label class="form-label">
          Tên danh mục
          <form:input path="name" cssClass="form-input" placeholder="Ví dụ: Cà phê Arabica"/>
          <form:errors path="name" cssClass="field-error"/>
        </label>

        <label class="form-label">
          Hình ảnh (tên file hoặc URL)
          <form:input path="images" cssClass="form-input" placeholder="arabica.jpg"/>
          <form:errors path="images" cssClass="field-error"/>
        </label>

        <label class="form-label">
          Trạng thái
          <form:select path="status" cssClass="form-input">
            <form:option value="1">Hoạt động</form:option>
            <form:option value="0">Tạm ẩn</form:option>
          </form:select>
        </label>

        <div style="display:flex;gap:0.75rem;margin-top:0.5rem;">
          <button type="submit" class="btn-primary-pill" style="flex:1;">Lưu danh mục</button>
          <a href="<c:url value='/admin/categories/searchpaginated'/>" class="btn-outline-pill">Quay lại</a>
        </div>
      </form:form>
    </div>
  </div>
</body>
</html>
