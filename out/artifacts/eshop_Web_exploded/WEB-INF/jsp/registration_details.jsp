<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<html>
<head>
    <title>Additional data registration</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2><fmt:message key="page.registration_details.registration_additional_data"/></h2> <br>
<form action="/registration-details" method="post">
    <label for="name"> <fmt:message key="page.registration_details.name"/>:
        <input type="text" name="name" id="name">
    </label> <br>
    <label for="surname"> <fmt:message key="page.registration_details.surname"/>:
        <input type="text" name="surname" id="surname">
    </label> <br>
    <label for="birthday"> <fmt:message key="page.registration_details.birthday"/>:
        <input type="date" name="birthday" id="birthday">
    </label> <br>
    <label for="phone"> <fmt:message key="page.registration_details.phone"/>:
        <input type="tel" name="phone" id="phone">
    </label> <br>
    <button type="submit"><fmt:message key="page.registration_details.register"/></button>
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