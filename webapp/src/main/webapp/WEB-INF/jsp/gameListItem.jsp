<%--
    Include this page:
        <%@ include file="gameListItem.jsp"%>
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="game-list-item">
    <div class="game-list-item-action">
        <form method="post" onsubmit="var buttons = document.getElementsByClassName('button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].disabled=true;}">
            <input type="hidden" name="gameId" value="<c:out value="${game.id}"/>">
            <spring:message code="game.addToBacklog" var="addToBacklog"/>
            <spring:message code="game.addingToBacklog" var="addingToBacklog"/>
            <spring:message code="game.removeFromBacklog" var="removeFromBacklog"/>
            <spring:message code="game.removingFromBacklog" var="removingFromBacklog"/>
            <c:choose>
            	<c:when test="${game.inBacklog}">
            		<input class="game-backlog-submit button-${game.id} remove-button-${game.id}" type="submit" onclick="var buttons = document.getElementsByClassName('remove-button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].value = '${removingFromBacklog}';}" value="${removeFromBacklog}"/>
            	</c:when>
            	<c:otherwise>
            		<input class="game-backlog-submit button-${game.id} add-button-${game.id}" type="submit" onclick="var buttons = document.getElementsByClassName('add-button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].value = '${addingToBacklog}';}" value="${addToBacklog}"/>
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
