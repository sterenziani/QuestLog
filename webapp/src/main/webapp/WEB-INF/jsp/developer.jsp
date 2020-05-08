<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
        <div>
	        <spring:message code="developer.gamesFromDeveloper" arguments="${developer.name}" var="gamesFromDeveloper"/>
	        <c:set var="games" value="${developer.games}"/>
	        <c:set var="listName" value="${gamesFromDeveloper}"/>
	        <%@ include file="gameList.jsp"%>
    	</div>
    </div>
</body>
</html>