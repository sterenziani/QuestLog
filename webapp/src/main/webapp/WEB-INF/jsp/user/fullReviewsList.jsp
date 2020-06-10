<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <spring:message code="user.reviews" arguments="${user.username}"/></title>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
    	<spring:message code="user.reviews" arguments="${user.username}" var="reviewsListName"/>
    	<spring:message code="user.noReviews" var="emptyListMessage"/>
	    <%@ include file="../common/reviewsList.jsp"%>
	    <%@ include file="../common/pageNumbers.jsp"%>
	</div>
</body>
</html>