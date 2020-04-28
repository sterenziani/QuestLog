<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="commonHead.jsp"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/game.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetails.css"/>">
</head>
<body>
    <%@ include file="navigation.jsp"%>
    <div class="content">
		<div class="game">
			<div class="game-backlog">
				<form method="post">
					<input type="hidden" name="gameId" value="${game.id}">
					<c:choose>
						<c:when test="${game.inBacklog}">
							<spring:message code="game.removeFromBacklog" var="removeFromBacklog"/>
							<input class="game-backlog-submit" type="submit" value="${removeFromBacklog}"/>
						</c:when>
						<c:otherwise>
							<spring:message code="game.addToBacklog" var="addToBacklog"/>
							<input class="game-backlog-submit" type="submit" value="${addToBacklog}"/>
						</c:otherwise>
					</c:choose>
				</form>
			</div>
			<div class="game-title">
				<h2>${game.title}</h2>
			</div>
			<%@include file="gameDetails.jsp"%>
		</div>
		<div class="game-details-score">
			<strong><spring:message code="game.score"/></strong>
			<form:form name="scores" method="POST" action="scores/${game.id}">
				<spring:message code="game.score" var="score"/>

				<c:choose>
				<c:when test="${empty user_score}">
					<div class="score-slider">
						<input type="range" min="0" max="100" id="score" class="slider-range"
							oninput="scoreWrite.value = score.value" value="0"
							name="score"/>
					</div>
					<div class="score-number">
						<input type="number" id="scoreWrite" name="scoreWrite" value="NONE"
							oninput="score.value = scoreWrite.value" min="0" max="100" step="1"/>
					</div>
				</c:when>
				<c:otherwise>
					<div class="score-slider">
						<input type="range" min="0" max="100" id="score" class="slider-range"
							oninput="scoreWrite.value = score.value" value="${user_score.score}"
							name="score"/>
					</div>
					<div class="score-number">
						<input type="number" id="scoreWrite" name="scoreWrite" value="${user_score.score}"
							oninput="score.value = scoreWrite.value" min="0" max="100" step="1"/>
					</div>
				</c:otherwise>
				</c:choose>
				<input type="hidden" value="${game.id}" name="game"/>
				<div class="score-submit">
					<input type="submit" class="score-submit-button" value="<spring:message code="game.submit"/>"/>
				</div>
				<div></div>
			</form:form>
			<div>
				<spring:message code="game.averageUserScore"/> ${averageScore}
			</div>
		</div>
		<div class="game-table-run">
			<table class="runs-table">
				<tr>
				<th><spring:message code="game.playstyle"/></th>
				<th><spring:message code="game.average"/></th>
				</tr>
				<c:forEach var="element" items="${playAverage}">
					<tr>
						<td><spring:message code="playstyle.${element.key}"/></td>
						<td>${element.value}</td>
					</tr>
				</c:forEach>
			</table>
			<br><br>
			<c:if test="${loggedUser != null}">
				<table class="runs-table">
					<tr>
					<th><spring:message code="game.platform"/></th>
					<th><spring:message code="game.playstyle"/></th>
					<th><spring:message code="game.time"/></th>
					</tr>
					<c:forEach var="element" items="${user_runs}">
						<tr>
							<td><c:out value="${element.platform.shortName}"/></td>
							<td><spring:message code="playstyle.${element.playstyle.name}"/></td>
							<td><c:out value="${element}"/></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
			<a class="create-run-button" href="<c:url value="/createRun/${game.id}"/>"><spring:message code="game.addRun"/></a>
		</div>
	</div>
</body>
</html>