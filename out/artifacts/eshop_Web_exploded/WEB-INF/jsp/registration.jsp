<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"  %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<html>
<head>
    <title>Main data registration</title>
</head>
<body>
<%@include file="header.jsp"%>
<h2><fmt:message key="page.registration.registration_main_data"/></h2> <br>
    <form action="/registration" method="post">
        <label for="username"> <fmt:message key="page.registration.username"/>:
            <input type="text" name="username" id="username">
        </label> <br>
        <label for="email"> <fmt:message key="page.registration.email"/>:
            <input type="text" name="email" id="email">
        </label> <br>
        <label for="password"> <fmt:message key="page.registration.password"/>:
            <input type="password" name="password" id="password">
        </label> <br>
        <fmt:message key="page.registration.role"/>:
        <select name="role" id="role">
            <c:forEach var="role" items="${requestScope.roles}">
                <option value="${role}">${role}</option>
            </c:forEach>
        </select> <br>
        <button type="submit"><fmt:message key="page.registration.register"/></button>
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
