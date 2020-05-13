<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="content">	
	    <div class="main-game-lists-backlog">
	    	<spring:message code="user.backlog" arguments="${user.username}" var="myBacklog"/>
	        <c:set var="listName" value="${myBacklog}"/>
	        <c:set var="games" value="${backlog}"/>
			<c:set var="seeAllUrl" value="/backlog/${user.id}"/>
	        <%@ include file="../common/gameList.jsp"%>
			<c:remove var="seeAllUrl"/>
	    </div>
		
		<c:set var="scoresInPage" value="${scoresInPage}"/>
		<c:set var="seeAllScoresUrl" value="/users/${user.id}/scores"/>
		<%@ include file="../common/scoresList.jsp"%>

		<c:set var="runsInPage" value="${runsInPage}"/>
		<c:set var="seeAllRunsUrl" value="/users/${user.id}/runs"/>
		<%@ include file="../common/runsList.jsp"%>
	</div>
</body>
</html>
