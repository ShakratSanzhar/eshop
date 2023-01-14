<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<html>
<head>
    <title>Cart</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2><fmt:message key="page.cart.cart"/></h2> <br>
<c:if test="${not empty requestScope.success}">
    <div style="color:green">
        ${requestScope.success}
    </div>
</c:if>
<fmt:message key="page.cart.total"/>: ${requestScope.cart.price} T.
        <c:forEach var="products" items="${requestScope.products}">
            <div>
                <fmt:message key="page.cart.book_name"/>: ${products.product.name} - ${products.quantity} <fmt:message key="page.cart.piece"/>
            </div>
        </c:forEach>
</body>
</html>
