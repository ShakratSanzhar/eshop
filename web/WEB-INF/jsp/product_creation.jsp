<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<html>
<head>
    <title>New book creation</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2><fmt:message key="page.product_creation.add_new_book"/></h2> <br>
<form action="${pageContext.request.contextPath}/new-product" method="post" enctype="multipart/form-data">
    <fmt:message key="page.product_creation.category"/>:
    <select name="category" id="category">
        <c:forEach var="category" items="${requestScope.categories}">
            <option value="${category.name}">${category.name}</option>
        </c:forEach>
    </select> <br>
    <label for="name"> <fmt:message key="page.product_creation.name"/>:
        <input type="text" name="name" id="name">
    </label> <br>
    <label for="description"> <fmt:message key="page.product_creation.description"/>:
        <input type="text" name="description" id="description">
    </label> <br>
    <label for="author"> <fmt:message key="page.product_creation.author"/>:
        <input type="text" name="author" id="author">
    </label> <br>
    <label for="publisher"> <fmt:message key="page.product_creation.publisher"/>:
        <input type="text" name="publisher" id="publisher">
    </label> <br>
    <label for="publishingYear"> <fmt:message key="page.product_creation.publishing_year"/>:
        <input type="date" name="publishingYear" id="publishingYear">
    </label> <br>
    <label for="image"> <fmt:message key="page.product_creation.image"/>:
        <input type="file" name="image" id="image">
    </label> <br>
    <label for="price"> <fmt:message key="page.product_creation.price"/>:
        <input type="number" name="price" id="price">
    </label> <br>
    <label for="remainingAmount"> <fmt:message key="page.product_creation.remaining_amount"/>:
        <input type="number" name="remainingAmount" id="remainingAmount">
    </label> <br>
    <label for="pageCount"> <fmt:message key="page.product_creation.page_count"/>:
        <input type="number" name="pageCount" id="pageCount">
    </label> <br>
    <button type="submit"><fmt:message key="page.product_creation.add"/></button>
    <c:if test="${not empty requestScope.errors}">
        <div style="color:red">
            <c:forEach var="error" items="${requestScope.errors}">
                <span>${error.message}</span>
            </c:forEach>
        </div>
    </c:if>
</form>
</body>
</html>