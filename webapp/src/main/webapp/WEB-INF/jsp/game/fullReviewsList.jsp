<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <spring:message code="game.reviews" arguments="${game.title}"/></title>
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