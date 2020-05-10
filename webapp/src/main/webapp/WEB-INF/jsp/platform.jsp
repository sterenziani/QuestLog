<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
    	<div class="logo"><img class="page-header-image" src="${platform.logo}"/></div>
        <div>
	        <spring:message code="platform.gamesForPlatform" arguments="${platform.name}" var="gamesForPlatform"/>
	        <c:set var="games" value="${platform.games}"/>
	        <c:set var="listName" value="${gamesForPlatform}"/>
	        <%@ include file="gameList.jsp"%>
    	</div>
    </div>
</body>
</html>