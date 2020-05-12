<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <c:if test="${!empty seeAllScoresUrl}">
        <div class="ml-auto">
            <a class="btn btn-link" href="<c:url value="${seeAllScoresUrl}"/>"><spring:message code="explore.seeAll"/></a>
        </div>
    </c:if>
	<div>
		<h2><spring:message code="user.scores" arguments="${user.username}"/></h2>
		<c:if test="${empty scoresInPage}">
           	<p><spring:message code="user.noScores"/></p>
       	</c:if>
		<c:forEach var="element" items="${scoresInPage}">
			<div class="user-avg-score">
				<strong class="score-title">
					<a href="<c:url value="/games/${element.game.id}"/>">${element.game}</a>:
						<p class="score-display score-display-avg">${element.score}</p>
				</strong>
			</div>
		</c:forEach>
	</div>