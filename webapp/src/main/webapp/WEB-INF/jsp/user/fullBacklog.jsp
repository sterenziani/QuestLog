<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <spring:message code="index.myBacklog"/></title>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
	    <div class="main-game-lists-backlog">
	    	<spring:message code="index.myBacklog" var="myBacklog"/>
	        <c:set var="listName" value="${myBacklog}"/>
	        <c:set var="games" value="${gamesInPage}"/>
	        <%@ include file="../common/gameList.jsp"%>
			<c:choose>
				<c:when test="${visitedUser == null}}">
					<c:url value="/backlog" var="listPath"/>
				</c:when>
				<c:otherwise>
					<c:url value="/backlog/${visitedUser.id}" var="listPath"/>
				</c:otherwise>
			</c:choose>
	        <%@ include file="../common/pageNumbers.jsp"%>
	    </div>
	</div>
</body>
</html>
