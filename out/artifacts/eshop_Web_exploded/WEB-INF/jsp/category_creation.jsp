<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<html>
<head>
    <title>New category creation</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2><fmt:message key="page.category_creation.new_category"/></h2> <br>
<form action="/new-category" method="post">
    <label for="name"> <fmt:message key="page.category_creation.category_name"/>:
        <input type="text" name="name" id="name">
    </label> <br>
    <fmt:message key="page.category_creation.parent_category"/>:
    <select name="parentCategory" id="parentCategory">
        <option value="Main category"><fmt:message key="page.category_creation.root_category"/></option>
        <c:forEach var="category" items="${requestScope.categories}">
            <option value="${category.name}">${category.name}</option>
        </c:forEach>
    </select> <br>
    <button type="submit"><fmt:message key="page.category_creation.send"/></button>
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