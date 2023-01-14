<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<div>
    <c:if test="${not empty sessionScope.user}">
        <div id="logout">
            <form action="${pageContext.request.contextPath}/logout" method="post">
                <button type="submit">logout</button>
            </form>
        </div>
    </c:if>
    <div id="locale">
        <form action="/locale" method="post">
            <button type="submit" name="lang" value="ru_RU">RU</button>
            <button type="submit" name="lang" value="en_US">EN</button>
        </form>
    </div>
    <div id="cart">
        <form action="/cart" method="get">
            <button type="submit">Cart</button>
        </form>
    </div>
    <fmt:setLocale value="${sessionScope.lang!=null ? sessionScope.lang : (param.lang!=null ? param.lang : 'en_US')}"/>
    <fmt:setBundle basename="translations"/>
</div>