<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title><sitemesh:write property="title"/></title>
  <link rel="icon" type="image/svg+xml" href="<c:url value='/static/favicon.svg'/>"/>
  <link rel="stylesheet" href="<c:url value='/static/css/app.css'/>"/>
  <sitemesh:write property="head"/>
</head>
<body>
  <jsp:include page="/commons/web/header.jsp"/>
  <main class="site-main">
    <sitemesh:write property="body"/>
  </main>
  <jsp:include page="/commons/web/footer.jsp"/>
</body>
</html>
