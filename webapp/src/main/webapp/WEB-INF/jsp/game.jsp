<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body>
    <%@ include file="navigation.jsp"%>
    <div class="content background-color">
		<div class="game">
			<div class="game-backlog">
				<form method="post" onsubmit="var buttons = document.getElementsByClassName('button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].disabled=true;}">
					<input type="hidden" name="gameId" value="${game.id}">
					<spring:message code="game.addToBacklog" var="addToBacklog"/>
		            <spring:message code="game.addingToBacklog" var="addingToBacklog"/>
		            <spring:message code="game.removeFromBacklog" var="removeFromBacklog"/>
		            <spring:message code="game.removingFromBacklog" var="removingFromBacklog"/>
					<c:choose>
		            	<c:when test="${game.inBacklog}">
		            		<input class="game-backlog-submit button-${game.id} remove-button-${game.id}" type="submit" onclick="var buttons = document.getElementsByClassName('remove-button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].value = '${removingFromBacklog}';}" value="${removeFromBacklog}"/>
		            	</c:when>
		            	<c:otherwise>
		            		<input class="game-backlog-submit button-${game.id} add-button-${game.id}" type="submit" onclick="var buttons = document.getElementsByClassName('add-button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].value = '${addingToBacklog}';}" value="${addToBacklog}"/>
		            	</c:otherwise>
		            </c:choose>
				</form>
			</div>
			<div class="game-title">
				<h2>${game.title}</h2>
			</div>
			<%@include file="gameDetails.jsp"%>
		</div>
		<div>
		<div class="game-details-description">
            <p><c:out value="${game.description}"/></p>
        </div>
		<div class="game-details-score">
			<form:form name="scores" method="POST" action="scores/${game.id}">
				
					<strong class="score-title"><spring:message code="game.score"/></strong>
					<spring:message code="game.score" var="score"/>
					<c:choose>
						<c:when test="${empty user_score}">
							<div class="score-slider">
								<input type="range" min="0" max="100" id="score" class="slider-range"
								oninput="scoreText.innerHTML = score.value" value="0"
								name="score"/>
							</div>
							<div class="score-number">
								<p class="score-display" id="scoreText">-</p>
							</div>
							<div class="score-submit">
								<input type="submit" class="score-submit-button button" value="<spring:message code="game.rate"/>"/>
							</div>
						</c:when>
						<c:otherwise>
							<div class="score-slider">
								<input type="range" min="0" max="100" id="score" class="slider-range"
								oninput="scoreText.innerHTML = score.value" value="${user_score.score}"
								name="score"/>
							</div>
							<div class="score-number">
								<p class="score-display" id="scoreText">${user_score.score}</p>
							</div>
							<div class="score-submit">
								<input type="submit" class="score-submit-button button" value="<spring:message code="game.rate"/>"/>
							</div>
						</c:otherwise>
					</c:choose>
					
					<input type="hidden" value="${game.id}" name="game"/>
			</form:form>
			<div class="user-avg-score">
				<strong class="score-title">
					<spring:message code="game.averageUserScore"/>
					<c:choose>
						<c:when test="${empty averageScore}">
							<p class="score-display score-display-avg">-</p>
						</c:when>
						<c:otherwise>
							<p class="score-display score-display-avg">${averageScore}</p>
						</c:otherwise>
					</c:choose>
				</strong>
			</div>
		</div>
		</div>
		<div class="game-table-run">
			<table class="runs-table">
				<tr>
				<th><spring:message code="game.playstyle"/></th>
				<th><spring:message code="game.averageTime"/></th>
				</tr>
				<c:forEach var="element" items="${playAverage}">
					<tr>
						<td><spring:message code="playstyle.${element.key}"/></td>
						<td>${element.value}</td>
					</tr>
				</c:forEach>
			</table>
			<br><br>
			<c:if test="${loggedUser != null && !empty user_runs}">
				<table class="runs-table">
					<tr>
					<th><spring:message code="game.platform"/></th>
					<th><spring:message code="game.playstyle"/></th>
					<th width="130px"><spring:message code="game.yourTime"/></th>
					</tr>
					<c:forEach var="element" items="${user_runs}">
						<tr>
							<td><c:out value="${element.platform.shortName}"/></td>
							<td><spring:message code="playstyle.${element.playstyle.name}"/></td>
							<td width="130px"><c:out value="${element}"/></td>
						</tr>
					</c:forEach>
				</table>
			</c:if>
			<a class="create-run-button button" href="<c:url value="/createRun/${game.id}"/>"><spring:message code="game.addRun"/></a>
		</div>
	</div>
</body>
</html>