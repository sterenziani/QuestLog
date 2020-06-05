<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - ${user.username}</title>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
	    <div>

	    </div>
	    <div class="container text-center align-middle text-white">
	    	<div class="my-5 py-5 bg-dark border-bottom border-primary rounded-lg">
	    		<h1 class="align-middle share-tech-mono"><c:out value="${user.username}"/></h1>
	    		<c:if test="${loggedUser.id == user.id}">
		    		<h5 class="align-middle">(<c:out value="${user.email}"/>)</h5>
		    	</c:if>
		    		<spring:message code="user.gamesRated" arguments="${user.scoreCount}"/><br>
		    		<spring:message code="user.averageScore" arguments="${user.scoreAverage}"/><br>
		    		<spring:message code="user.createdRuns" arguments="${user.runCount}"/><br>
		    		<spring:message code="user.hoursPlayed" arguments="${user.totalHoursPlayed}"/>
		    		<c:if test="${user.favoriteGame != null}">
			    		<c:set var="favGame" value="${user.favoriteGame}"/><br>
			    		<spring:message code="user.favoriteGame" arguments="${favGame}"/>
		    		</c:if>
	        </div>
	    </div>
	    
	    <div class="main-game-lists-backlog">
	    	<spring:message code="user.backlog" arguments="${user.username}" var="myBacklog"/>
	        <c:set var="listName" value="${myBacklog}"/>
	        <c:set var="games" value="${backlog}"/>
	        <c:if test="${backlogCropped}">
				<c:set var="seeAllUrl" value="/backlog/${user.id}"/>
			</c:if>
	        <%@ include file="../common/gameList.jsp"%>
			<c:remove var="seeAllUrl"/>
	    </div>
		
		<c:set var="scoresInPage" value="${scoresInPage}"/>
		<c:if test="${scoresCropped}">
			<c:set var="seeAllScoresUrl" value="/users/${user.id}/scores"/>
		</c:if>
		<%@ include file="../common/scoresList.jsp"%>

		<c:set var="runsInPage" value="${runsInPage}"/>
		<c:if test="${runsCropped}">
			<c:set var="seeAllRunsUrl" value="/users/${user.id}/runs"/>
		</c:if>
		<%@ include file="../common/runsList.jsp"%>
	</div>
</body>
</html>
