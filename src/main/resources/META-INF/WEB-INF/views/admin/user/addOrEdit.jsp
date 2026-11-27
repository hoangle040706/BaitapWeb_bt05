<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
  <title>${isEdit ? 'Cập nhật người dùng' : 'Thêm người dùng'}</title>
</head>
<body>
  <div class="auth-container" style="max-width:38rem;">
    <h1 class="page-heading">${isEdit ? 'Cập nhật người dùng' : 'Thêm người dùng'}</h1>
    <p class="page-subtitle">Quản lý thông tin tài khoản, vai trò và trạng thái hoạt động.</p>

    <c:if test="${not empty errorMessage}">
      <div class="alert alert-error" role="alert"><c:out value="${errorMessage}"/></div>
    </c:if>

    <div class="panel-card">
      <c:url var="formAction" value="${isEdit ? '/admin/users/update' : '/admin/users/create'}"/>
      <form:form action="${formAction}" method="post" modelAttribute="userRequest" cssClass="form-stack">
        <c:if test="${isEdit}">
          <form:hidden path="id"/>
        </c:if>

        <div class="form-grid-2">
          <label class="form-label">
            Tên đăng nhập
            <form:input path="username" cssClass="form-input" placeholder="nguyenvana"/>
            <form:errors path="username" cssClass="field-error"/>
          </label>

          <label class="form-label">
            Họ và tên
            <form:input path="fullname" cssClass="form-input" placeholder="Nguyễn Văn A"/>
            <form:errors path="fullname" cssClass="field-error"/>
          </label>
        </div>

        <div class="form-grid-2">
          <label class="form-label">
            Email
            <form:input path="email" type="email" cssClass="form-input" placeholder="email@rangxay.vn"/>
            <form:errors path="email" cssClass="field-error"/>
          </label>

          <label class="form-label">
            Số điện thoại
            <form:input path="phone" cssClass="form-input" placeholder="0901234567"/>
            <form:errors path="phone" cssClass="field-error"/>
          </label>
        </div>

        <div class="form-grid-2">
          <label class="form-label">
            Vai trò
            <form:select path="role" cssClass="form-input">
              <c:forEach items="${roles}" var="r">
                <form:option value="${r}">${r}</form:option>
              </c:forEach>
            </form:select>
            <form:errors path="role" cssClass="field-error"/>
          </label>

          <label class="form-label">
            Trạng thái
            <form:select path="status" cssClass="form-input">
              <form:option value="1">Hoạt động</form:option>
              <form:option value="0">Tạm khóa</form:option>
            </form:select>
            <form:errors path="status" cssClass="field-error"/>
          </label>
        </div>

        <label class="form-label">
          Ảnh đại diện (URL hoặc tên file)
          <form:input path="avatar" cssClass="form-input" placeholder="avatar.png (tùy chọn)"/>
          <form:errors path="avatar" cssClass="field-error"/>
        </label>

        <div class="form-grid-2">
          <label class="form-label">
            Mật khẩu ${isEdit ? '(để trống nếu giữ nguyên)' : ''}
            <form:password path="password" cssClass="form-input" autocomplete="new-password" placeholder="Tối thiểu 6 ký tự"/>
            <form:errors path="password" cssClass="field-error"/>
          </label>

          <label class="form-label">
            Nhập lại mật khẩu
            <form:password path="confirmPassword" cssClass="form-input" autocomplete="new-password" placeholder="Xác nhận mật khẩu"/>
            <form:errors path="confirmPassword" cssClass="field-error"/>
          </label>
        </div>

        <div style="display:flex;gap:0.75rem;margin-top:0.5rem;">
          <button type="submit" class="btn-primary-pill" style="flex:1;">
            ${isEdit ? 'Cập nhật tài khoản' : 'Tạo tài khoản'}
          </button>
          <a href="<c:url value='/admin/users/searchpaginated'/>" class="btn-outline-pill">Quay lại</a>
        </div>
      </form:form>
    </div>
  </div>
</body>
</html>
