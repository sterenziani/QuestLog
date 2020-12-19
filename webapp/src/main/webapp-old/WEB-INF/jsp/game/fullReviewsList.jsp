<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
	<spring:message code="game.reviews" arguments="${game.title}" var="title"/>
    <title>QuestLog - <c:out value="${title}"/></title>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
    	<spring:message code="game.reviewsForGame" arguments="${game.title}" var="reviewsListName"/>
    	<spring:message code="game.noReviews" var="emptyListMessage"/>
	    <%@ include file="../common/reviewsList.jsp"%>
	    <%@ include file="../common/pageNumbers.jsp"%>
	</div>
</body>
</html>