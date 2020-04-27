<%--
    Include this page:
        <%@ include file="gameDetails.jsp"%>

    Including jsp should have:
        * variable game
        * inside the header:
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetails.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetailsInfoItem.css"/>">

--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<div class="game-details">
    <div class="game-details-cover">
        <img src="<c:url value="${game.cover}"/>" alt="<c:out value="${game.title}"/>">
    </div>
    <div class="game-details-content">
        <div class="game-details-description">
            <p><c:out value="${game.description}"/></p>
        </div>
        <div class="game-details-info">
            <dl>
                <div class="game-details-release-dates">
                	<spring:message code="game.releaseDates" var="releaseDates"/>
                    <c:set var="title" value="${releaseDates}"/>
                    <c:set var="items" value="${game.releaseDates}"/>
                    <%@include file="gameDetailsInfoItem.jsp"%>
                </div>
                <div class="game-details-genres">
                	<spring:message code="game.genres" var="genres"/>
                    <c:set var="title" value="${genres}"/>
                    <c:set var="items" value="${game.genres}"/>
                    <%@include file="gameGenreDetailsInfoItem.jsp"%>
                </div>
                <div class="game-details-platforms">
                	<spring:message code="game.platforms" var="platforms"/>
                    <c:set var="title" value="${platforms}"/>
                    <c:set var="items" value="${game.platforms}"/>
                    <%@include file="gameDetailsInfoItem.jsp"%>
                </div>
                <div class="game-details-developers">
                	<spring:message code="game.developers" var="developers"/>
                    <c:set var="title" value="${developers}"/>
                    <c:set var="items" value="${game.developers}"/>
                    <%@include file="gameDetailsInfoItem.jsp"%>
                </div>
                <div class="game-details-publishers">
                	<spring:message code="game.publishers" var="publishers"/>
                    <c:set var="title" value="${publishers}"/>
                    <c:set var="items" value="${game.publishers}"/>
                    <%@include file="gameDetailsInfoItem.jsp"%>
                </div>
                
                    <div class="game-details-score">
                    <strong><spring:message code="game.score"/></strong>
                	<form:form name="scores" method="POST" action="scores/${game.id}">
                	    <spring:message code="game.score" var="score"/>
                  		<c:choose>
            			<c:when test="${empty user_score}">
                 			<input type="range" min="0" max="100" id="score" class="slider-range"
                 			oninput="scoreWrite.value = score.value" value="0" 
                 			name="score"/>
                 			<input type="number" id="scoreWrite" name="scoreWrite" value="NONE"
                 			oninput="score.value = scoreWrite.value" min="0" max="100" step="1"/>
                 		</c:when>
            			<c:otherwise>
                 			<input type="range" min="0" max="100" id="score" class="slider-range"
                 			oninput="scoreWrite.value = score.value" value="${user_score.score}" 
                 			name="score"/>
                 			<input type="number" id="scoreWrite" name="scoreWrite" value="${user_score.score}"
                 			oninput="score.value = scoreWrite.value" min="0" max="100" step="1"/>
            			</c:otherwise>
            			</c:choose>            			
                 		<input type="hidden" value="${game.id}" name="game"/>
                 		<input type="submit" value="<spring:message code="game.submit"/>"/>
                 		<div></div>
                	</form:form>
                	</div>

    </div>
</div>