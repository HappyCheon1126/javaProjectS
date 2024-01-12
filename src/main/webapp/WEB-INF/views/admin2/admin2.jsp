<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctp" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="utf-8" />
  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no" />
  <meta name="description" content="" />
  <meta name="author" content="" />
  <title>Dashboard - SB Admin</title>
  <link href="https://cdn.jsdelivr.net/npm/simple-datatables@7.1.2/dist/style.min.css" rel="stylesheet" />
  <jsp:include page="styles2.jsp" />
</head>
<body class="sb-nav-fixed">
<jsp:include page="admin2Top.jsp" />
<div id="layoutSidenav">
  <div id="layoutSidenav_nav">
    <jsp:include page="admin2Left.jsp" />
  </div>
  <div id="layoutSidenav_content">
    <main>
      <c:if test="${empty adminFlag || adminFlag == 'admin2'}"><jsp:include page="admin2Content.jsp" /></c:if>
      <c:if test="${adminFlag=='dbCategory'}"><jsp:include page="dbShop/dbCategory.jsp" /></c:if>
      <c:if test="${adminFlag=='dbProduct'}"><jsp:include page="dbShop/dbProduct.jsp" /></c:if>
      <c:if test="${adminFlag=='dbShopList'}"><jsp:include page="dbShop/dbShopList.jsp" /></c:if>
      <c:if test="${adminFlag=='dbOption'}"><jsp:include page="dbShop/dbOption.jsp" /></c:if>
      <c:if test="${adminFlag=='dbShopContent'}"><jsp:include page="dbShop/dbShopContent.jsp" /></c:if>
      <c:if test="${adminFlag=='login'}"><jsp:include page="etc/login.jsp" /></c:if>
      <c:if test="${adminFlag=='register'}"><jsp:include page="etc/register.jsp" /></c:if>
      <c:if test="${adminFlag=='password'}"><jsp:include page="etc/password.jsp" /></c:if>
      <c:if test="${adminFlag=='charts'}"><jsp:include page="etc/charts.jsp" /></c:if>
      <c:if test="${adminFlag=='tables'}"><jsp:include page="etc/tables.jsp" /></c:if>
      <c:if test="${adminFlag=='401'}"><jsp:include page="etc/401.jsp" /></c:if>
      <c:if test="${adminFlag=='404'}"><jsp:include page="etc/404.jsp" /></c:if>
      <c:if test="${adminFlag=='500'}"><jsp:include page="etc/500.jsp" /></c:if>
      <c:if test="${adminFlag=='payment'}"><jsp:include page="payment/payment.jsp" /></c:if>
      <c:if test="${adminFlag=='sample'}"><jsp:include page="payment/sample.jsp" /></c:if>
      <c:if test="${adminFlag=='paymentOk'}"><jsp:include page="payment/paymentOk.jsp" /></c:if>
		</main>
	</div>
</div>
</body>
</html>
