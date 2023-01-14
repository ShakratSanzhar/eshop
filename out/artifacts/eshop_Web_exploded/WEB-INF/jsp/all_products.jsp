<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<html>
<head>
    <title>All books</title>
    <style>
        * {
            box-sizing: border-box;
        }

        .product-item {
            width: 300px;
            text-align: center;
            margin: 0 auto;
            border-bottom: 2px solid #F5F5F5;
            background: white;
            font-family: "Open Sans";
            transition: .3s ease-in;
        }

        .product-item:hover {
            border-bottom: 2px solid #fc5a5a;
        }

        .product-item img {
            display: block;
            width: 100%;
        }

        .product-list {
            background: #fafafa;
            padding: 15px 0;
        }

        .product-list h3 {
            font-size: 18px;
            font-weight: 400;
            color: #444444;
            margin: 0 0 10px 0;
        }

        .price {
            font-size: 16px;
            color: #fc5a5a;
            display: block;
            margin-bottom: 12px;
        }

        .button {
            text-decoration: none;
            display: inline-block;
            padding: 0 12px;
            background: #cccccc;
            color: white;
            text-transform: uppercase;
            font-size: 12px;
            line-height: 28px;
            transition: .3s ease-in;
        }

        .product-item:hover .button {
            background: #fc5a5a;
        }
    </style>
</head>
<body>
<%@include file="header.jsp" %>
<h2><fmt:message key="page.all_products.all_books"/></h2> <br>
<form action="/all-products" method="get">
    <fmt:message key="page.all_products.book_category"/>:
    <select name="category" id="category">
        <option value=""></option>
        <c:forEach var="category" items="${requestScope.categories}">
            <option value="${category.name}">${category.name}</option>
        </c:forEach>
    </select> <br>
    <label for="name"> <fmt:message key="page.all_products.name"/>:
        <input type="text" name="name" id="name">
    </label> <br>
    <label for="author"> <fmt:message key="page.all_products.author"/>:
        <input type="text" name="author" id="author">
    </label> <br>
    <label for="publisher"> <fmt:message key="page.all_products.publisher"/>:
        <input type="text" name="publisher" id="publisher">
    </label> <br>
    <label for="price"> <fmt:message key="page.all_products.price"/>:
        <input type="number" name="price" id="price">
    </label> <br>
    <button type="submit"><fmt:message key="page.all_products.show"/></button>
</form>
<c:forEach var="product" items="${requestScope.products}">
    <div class="product-item">
        <img src="${pageContext.request.contextPath}/images/${product.image}" alt="Product image">
        <div class="product-list">
            <a href="/about-product?id=${product.id}"> <h3>${product.name}</h3></a>
            <span class="price">T ${product.price}</span>

            <form action="/add-product-to-cart" method="post">
                <label for="count"> <fmt:message key="page.all_products.quantity"/>:
                    <input type="number" name="count" id="count">
                </label> <br>
            <button class="button" type="submit" name="product" value="${product.id}"><fmt:message key="page.all_products.to_cart"/></button>
            </form>
        </div>
    </div>
</c:forEach>

<c:if test="${currentPage != 1}">
    <td><a href="all-products?page=${currentPage - 1}&category=${requestScope.category}&name=${requestScope.name}&author=${requestScope.author}&publisher=${requestScope.publisher}&price=${requestScope.price}"><fmt:message key="page.all_products.previous"/></a></td>
</c:if>

<table>
    <tr>
        <c:forEach begin="1" end="${noOfPages}" var="i">
            <c:choose>
                <c:when test="${currentPage eq i}">
                    <td>${i}</td>
                </c:when>
                <c:otherwise>
                    <td><a href="all-products?page=${i}&category=${requestScope.category}&name=${requestScope.name}&author=${requestScope.author}&publisher=${requestScope.publisher}&price=${requestScope.price}">${i}</a></td>
                </c:otherwise>
            </c:choose>
        </c:forEach>
    </tr>
</table>

<c:if test="${currentPage lt noOfPages}">
    <td><a href="all-products?page=${currentPage + 1}&category=${requestScope.category}&name=${requestScope.name}&author=${requestScope.author}&publisher=${requestScope.publisher}&price=${requestScope.price}"><fmt:message key="page.all_products.next"/></a></td>
</c:if>
</body>
</html>
