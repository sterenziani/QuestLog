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
        <div class="game-title">
            <h2>${game.title}</h2>
        </div>
        <div class="game-table-run">
                    <a class="create-run-button" href="<c:url value="/createRun/${game.id}"/>"><spring:message code="game.addRun"/></a>
            		<table style="width:100%">
  						<tr>
    					<th><spring:message code="game.playstyle"/></th>
    					<th><spring:message code="game.average"/></th>
    					</tr>
    					<c:forEach var="element" items="${playAverage}">
  						<tr>
   						<td>${element.key}</td>
    					<td>${element.value}</td>
    					</tr>
 						</c:forEach>
						</table>
            </dl>
        </div>
        <%@include file="gameDetails.jsp"%>
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
    </div>
</body>
</html>