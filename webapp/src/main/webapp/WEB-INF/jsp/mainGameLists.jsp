<%--
    Include this page:
        <%@ include file="mainGameLists.jsp"%>

    Including jsp should have:
        * variable game
        * inside the header:
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">

--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="main-game-lists">
    <div class="main-game-lists-backlog">
    	<spring:message code="index.myBacklog" var="myBacklog"/>
        <c:set var="listName" value="${myBacklog}"/>
        <c:set var="games" value="${backlogGames}"/>
        <%@ include file="gameList.jsp"%>
    </div>

    <%-- Recommended games will be available later on

    <div class="main-game-lists-recommended">
        <%@ include file="gameList.jsp"%>
    </div>
    --%>

    <div class="main-game-lists-upcoming">
    	<spring:message code="index.upcoming" var="upcoming"/>
        <c:set var="listName" value="${upcoming}"/>
        <c:set var="games" value="${upcomingGames}"/>
        <%@ include file="gameList.jsp"%>
    </div>
</div>