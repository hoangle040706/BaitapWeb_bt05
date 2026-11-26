<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
  <title>Tạo tài khoản</title>
</head>
<body>
  <div class="auth-container">
    <h1 class="page-heading">Tạo tài khoản</h1>
    <p class="page-subtitle">Đăng ký để sử dụng hệ thống Rang Xay.</p>

    <c:if test="${not empty errorMessage}">
      <div class="alert alert-error" role="alert"><c:out value="${errorMessage}"/></div>
    </c:if>

    <div class="panel-card">
      <c:url var="registerUrl" value="/register"/>
      <form:form action="${registerUrl}" method="post" modelAttribute="registerRequest" cssClass="form-stack">
        <c:if test="${not empty next}">
          <input type="hidden" name="next" value="<c:out value='${next}'/>"/>
        </c:if>

        <label class="form-label">
          Họ và tên
          <form:input path="fullname" cssClass="form-input" autocomplete="name" placeholder="Nguyễn Văn A"/>
          <form:errors path="fullname" cssClass="field-error"/>
        </label>

        <label class="form-label">
          Tên đăng nhập
          <form:input path="username" cssClass="form-input" autocomplete="username" placeholder="nguyenvana"/>
          <form:errors path="username" cssClass="field-error"/>
        </label>

        <label class="form-label">
          Email
          <form:input path="email" type="email" cssClass="form-input" autocomplete="email" placeholder="email@example.com"/>
          <form:errors path="email" cssClass="field-error"/>
        </label>

        <label class="form-label">
          Mật khẩu
          <form:password path="password" cssClass="form-input" autocomplete="new-password" placeholder="Tối thiểu 6 ký tự"/>
          <form:errors path="password" cssClass="field-error"/>
        </label>

        <label class="form-label">
          Nhập lại mật khẩu
          <form:password path="confirmPassword" cssClass="form-input" autocomplete="new-password" placeholder="Nhập lại mật khẩu"/>
          <form:errors path="confirmPassword" cssClass="field-error"/>
        </label>

        <button type="submit" class="btn-primary-pill w-full">Tạo tài khoản</button>

        <p class="auth-switch">
          Đã có tài khoản?
          <a href="<c:url value='/login'/>" class="auth-link">Đăng nhập</a>
        </p>
      </form:form>
    </div>
  </div>
</body>
</html>
