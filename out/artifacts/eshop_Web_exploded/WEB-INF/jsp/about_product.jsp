<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <title>${requestScope.product.name}</title>
</head>
<body>
<%@include file="header.jsp" %>
<h2><fmt:message key="page.about_product.about_book"/></h2> <br>
<img src="${pageContext.request.contextPath}/images/${requestScope.product.image}" alt="Product image">
<br>
<fmt:message key="page.about_product.book_name"/>: ${requestScope.product.name} <br>
<fmt:message key="page.about_product.category"/>: ${requestScope.product.categoryName} <br>
<fmt:message key="page.about_product.description"/>: ${requestScope.product.description} <br>
<fmt:message key="page.about_product.author"/>: ${requestScope.product.author} <br>
<fmt:message key="page.about_product.publisher"/>: ${requestScope.product.publisher} <br>
<fmt:message key="page.about_product.publishing_year"/>: ${requestScope.product.publishingYear} <br>
<fmt:message key="page.about_product.price"/>: ${requestScope.product.price} <br>
<fmt:message key="page.about_product.remaining_amount"/>: ${requestScope.product.remainingAmount} <br>
<fmt:message key="page.about_product.page_count"/>: ${requestScope.product.pageCount} <br>
<form action="/add-product-to-cart" method="post">
    <label for="count"> <fmt:message key="page.about_product.amount"/>:
        <input type="number" name="count" id="count">
    </label> <br>
    <button class="button" type="submit" name="product" value="${requestScope.product.id}"><fmt:message key="page.about_product.cart"/></button>
</form>
</body>
</html>
