<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <title>${game.title}</title>
    <link rel="stylesheet" type="text/css" href="<c:out value="http://fonts.googleapis.com/css?family=Roboto"/>" >
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/game.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigation.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigationSearchBar.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetails.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetailsInfoItem.css"/>">
</head>
<body>
    <%@ include file="navigation.jsp"%>
    <div class="content">
        <div class="game-title">
            <h2>${game.title}</h2>
        </div>
        <%@include file="gameDetails.jsp"%>
        <div class="game-backlog">
            <form method="post">
                <input type="hidden" name="id" value="${game.id}">
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