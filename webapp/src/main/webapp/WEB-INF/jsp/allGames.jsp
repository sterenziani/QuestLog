<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@ include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
        <div class="all-games">
	        <spring:message code="index.allGames" var="allGames"/>
	        <c:set var="listName" value="${allGames}"/>
	        <%@ include file="gameList.jsp"%>
    	</div>
    </div>
</body>
</html>