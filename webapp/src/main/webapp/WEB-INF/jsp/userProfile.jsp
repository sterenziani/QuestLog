<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body>
    <%@include file="navigation.jsp"%>
    <div class="content">
		<h2><spring:message code="user.introduction" arguments="${user.username}"/></h2>
		
	    <div class="main-game-lists-backlog">
	    	<spring:message code="index.myBacklog" var="myBacklog"/>
	        <c:set var="listName" value="${myBacklog}"/>
	        <c:set var="games" value="${backlog}"/>
			<c:set var="seeAllUrl" value="/backlog/${user.id}"/>
	        <%@ include file="gameList.jsp"%>
			<c:remove var="seeAllUrl"/>
	    </div>
		
		<div>
			<h2><spring:message code="user.scores" arguments="${user.username}"/></h2>
			<c:if test="${empty user.scores}">
            	<p><spring:message code="user.noScores"/></p>
        	</c:if>
			<c:forEach var="element" items="${user.scores}">
				<div class="user-avg-score">
					<strong class="score-title">
						${element.game}:
							<p class="score-display score-display-avg">${element.score}</p>
					</strong>
				</div>
			</c:forEach>
		</div>
		<c:set var="runsInPage" value="${runsInPage}"/>
		<c:set var="seeAllRunsUrl" value="/users/${user.id}/runs"/>
		<%@ include file="runsList.jsp"%>
	</div>
</body>
</html>
