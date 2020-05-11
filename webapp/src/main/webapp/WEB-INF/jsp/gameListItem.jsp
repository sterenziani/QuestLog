<%--
    Include this page:
        <%@ include file="gameListItem.jsp"%>
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="card m-3 d-flex bg-transparent" style="width: 18rem;">
    <form method="post" onsubmit="var buttons = document.getElementsByClassName('button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].disabled=true;}">
        <input type="hidden" name="gameId" value="<c:out value="${game.id}"/>">
        <spring:message code="game.addToBacklog" var="addToBacklog"/>
        <spring:message code="game.addingToBacklog" var="addingToBacklog"/>
        <spring:message code="game.removeFromBacklog" var="removeFromBacklog"/>
        <spring:message code="game.removingFromBacklog" var="removingFromBacklog"/>
        <c:choose>
            <c:when test="${game.inBacklog}">
                <input class="btn btn-block btn-outline-danger btn-lg not-rounded-bottom button-${game.id} remove-button-${game.id}" type="submit" onclick="var buttons = document.getElementsByClassName('remove-button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].value = '${removingFromBacklog}';}" value="${removeFromBacklog}"/>
            </c:when>
            <c:otherwise>
                <input class="btn btn-block btn-outline-success btn-lg not-rounded-bottom button-${game.id} add-button-${game.id}" type="submit" onclick="var buttons = document.getElementsByClassName('add-button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].value = '${addingToBacklog}';}" value="${addToBacklog}"/>
            </c:otherwise>
        </c:choose>
    </form>
    <a class="d-flex flex-column flex-grow-1 text-white" href="<c:url value="/games/${game.id}"/>">
        <img class="card-img-top cover" src="<c:url value="/images/${game.cover}"/>" alt="<c:out value="${game.title}"/>"/>
        <div class="card-body bg-primary flex-grow-1">
            <h5><c:out value="${game.title}"/></h5>
        </div>
    </a>
</div>
