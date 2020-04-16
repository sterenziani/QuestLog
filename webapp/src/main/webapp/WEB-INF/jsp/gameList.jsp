<%--
    Include this page:
        <%@ include file="gameListItem.jsp"%>

    Including jsp should have:
        * variable games
        * inside the header:
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">

--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="game-list">
    <div class="game-list-header">
        <h2>${listName}</h2>
    </div>
    <div class="game-list-games">
        <c:if test="${empty games}">
            <p><spring:message code="gameList.empty"/></p>
        </c:if>
        <c:forEach var="game" items="${games}">
            <%@ include file="gameListItem.jsp"%>
        </c:forEach>
    </div>
</div>
