<%--
    Include this page:
        <%@ include file="mainGameLists.jsp"%>
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="main-game-lists">
	<c:if test="${!empty backlogGames}">
	    <div class="main-game-lists-backlog">
	    	<spring:message code="index.myBacklog" var="myBacklog"/>
	        <c:set var="listName" value="${myBacklog}"/>
	        <c:set var="games" value="${backlogGames}"/>
	        <%@ include file="gameList.jsp"%>
	    </div>
	</c:if>
    <%-- Recommended games will be available later on

    <div class="main-game-lists-recommended">
        <%@ include file="gameList.jsp"%>
    </div>
    --%>
    <c:if test="${loggedUser != null && !empty recommendedGames}">.
	    <div class="main-game-lists-recommended">
	    	<spring:message code="index.recommended" var="recommended"/>
	        <c:set var="listName" value="${recommended}"/>
	        <c:set var="games" value="${recommendedGames}"/>
	        <%@ include file="gameList.jsp"%>
	    </div>
   	</c:if>
   	
   	<c:if test="${!empty popularGames}">
		<div class="main-game-lists-popular">
			<spring:message code="index.popular" var="popular"/>
			<c:set var="listName" value="${popular}"/>
			<c:set var="games" value="${popularGames}"/>
			<%@ include file="gameList.jsp"%>
		</div>
	</c:if>
   	
    <div class="main-game-lists-upcoming">
    	<spring:message code="index.upcoming" var="upcoming"/>
        <c:set var="listName" value="${upcoming}"/>
        <c:set var="games" value="${upcomingGames}"/>
        <%@ include file="gameList.jsp"%>
    </div>
</div>