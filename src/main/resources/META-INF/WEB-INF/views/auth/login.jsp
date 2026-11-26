<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
  <title>Đăng nhập</title>
</head>
<body>
  <div class="auth-container">
    <h1 class="page-heading">Đăng nhập</h1>
    <p class="page-subtitle">Chào mừng bạn quay lại.</p>

    <c:if test="${not empty successMessage}">
      <div class="alert alert-success" role="status"><c:out value="${successMessage}"/></div>
    </c:if>
    <c:if test="${not empty errorMessage}">
      <div class="alert alert-error" role="alert"><c:out value="${errorMessage}"/></div>
    </c:if>

    <div class="panel-card">
      <c:url var="loginUrl" value="/login"/>
      <form:form action="${loginUrl}" method="post" modelAttribute="loginRequest" cssClass="form-stack">
        <c:if test="${not empty next}">
          <input type="hidden" name="next" value="<c:out value='${next}'/>"/>
        </c:if>

        <label class="form-label">
          Tên đăng nhập hoặc Email
          <form:input path="usernameOrEmail" cssClass="form-input" autocomplete="username" placeholder="admin hoặc admin@rangxay.vn"/>
          <form:errors path="usernameOrEmail" cssClass="field-error"/>
        </label>

        <label class="form-label">
          Mật khẩu
          <form:password path="password" cssClass="form-input" autocomplete="current-password" placeholder="Nhập mật khẩu"/>
          <form:errors path="password" cssClass="field-error"/>
        </label>

        <button type="submit" class="btn-primary-pill w-full">Đăng nhập</button>

        <p class="auth-switch">
          Chưa có tài khoản?
          <a href="<c:url value='/register'/>" class="auth-link">Đăng ký</a>
        </p>
      </form:form>

      <div class="demo-box">
        <strong>Tài khoản mẫu:</strong><br/>
        Admin: <code>admin</code> / <code>Admin@123</code><br/>
        User: <code>user</code> / <code>User@123</code>
      </div>
    </div>
  </div>
</body>
</html>
