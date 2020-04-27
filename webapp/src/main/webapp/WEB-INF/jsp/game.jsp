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
	    <div class="column-left">
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
     <div class="column-center">
                    <div class="game-details-score">
                    <strong><spring:message code="game.score"/></strong>
                	<form:form name="scores" method="POST" action="scores/${game.id}">
                	    <spring:message code="game.score" var="score"/>
                  		<c:choose>
            			<c:when test="${empty user_score}">
                 			<input type="range" min="0" max="100" id="score" class="slider-range"
                 			oninput="scoreWrite.value = score.value" value="0" 
                 			name="score"/>
                 			<input type="number" id="scoreWrite" name="scoreWrite" value="NONE"
                 			oninput="score.value = scoreWrite.value" min="0" max="100" step="1"/>
                 		</c:when>
            			<c:otherwise>
                 			<input type="range" min="0" max="100" id="score" class="slider-range"
                 			oninput="scoreWrite.value = score.value" value="${user_score.score}" 
                 			name="score"/>
                 			<input type="number" id="scoreWrite" name="scoreWrite" value="${user_score.score}"
                 			oninput="score.value = scoreWrite.value" min="0" max="100" step="1"/>
            			</c:otherwise>
            			</c:choose>            			
                 		<input type="hidden" value="${game.id}" name="game"/>
                 		<input type="submit" value="<spring:message code="game.submit"/>"/>
                 		<div></div>
                	</form:form>
                	</div>
     
     </div>
     <div class="column-right">
             <div class="game-table-run">
                    <a class="create-run-button" href="<c:url value="/createRun/${game.id}"/>"><spring:message code="game.addRun"/></a>
            		<table style="width:100%">
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
				</div>
     
     </div>
        
    </div>
    </div>
</body>
</html>