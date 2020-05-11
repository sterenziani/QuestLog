<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
    <c:if test="${!empty seeAllRunsUrl}">
        <div class="ml-auto">
            <a class="btn btn-link" href="<c:url value="${seeAllRunsUrl}"/>"><spring:message code="explore.seeAll"/></a>
        </div>
    </c:if>
   	<div class="game-table-run">
		<h2><spring:message code="user.runs" arguments="${user.username}"/></h2>
		<c:if test="${empty runsInPage}">
           	<p><spring:message code="user.noRuns"/></p>
       	</c:if>
		<table class="runs-table">
			<tr>
			<th><spring:message code="game.game"/></th>
			<th><spring:message code="game.platform"/></th>
			<th><spring:message code="game.playstyle"/></th>
			<th width="130px"><spring:message code="game.time"/></th>
			</tr>
			<c:forEach var="element" items="${runsInPage}">
				<tr>
					<td><a href="<c:url value="/games/${element.game.id}"/>">${element.game.title}</a></td>
					<td><c:out value="${element.platform.shortName}"/></td>
					<td><spring:message code="playstyle.${element.playstyle.name}"/></td>
					<td width="130px"><c:out value="${element}"/></td>
				</tr>
			</c:forEach>
		</table>
	</div>