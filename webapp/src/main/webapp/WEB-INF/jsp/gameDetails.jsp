<%--
    Include this page:
        <%@ include file="gameDetails.jsp"%>
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
            </dl>
        </div>
    </div>
</div>