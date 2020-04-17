<%--
    Include this page:
        <%@ include file="gameListItem.jsp"%>

    Including jsp should have:
        * variable game
        * inside the header:
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">

--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="game-list-item">
    <div class="game-list-item-action">
        <form method="post">
            <input type="hidden" name="id" value="<c:out value="${game.id}"/>">
            <spring:message code="game.addToBacklog" var="addToBacklog"/>
            <spring:message code="game.removeFromBacklog" var="removeFromBacklog"/>
            <c:choose>
            	<c:when test="${game.inBacklog}">
            		<input type="submit" value="${removeFromBacklog}"/>
            	</c:when>
            	<c:otherwise>
            		<input type="submit" value="${addToBacklog}"/>
            	</c:otherwise>
            </c:choose>
        </form>
    </div>
    <a href="<c:url value="/games/${game.id}"/>">
        <div class="game-list-item-content">
            <div class="game-list-item-cover">
                <img src="<c:url value="${game.cover}"/>" alt="${game.title}"/>
            </div>
            <div class="game-list-item-data">
                <h3><c:out value="${game.title}"/></h3>
            </div>
        </div>
    </a>
</div>
