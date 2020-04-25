<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
    	<div class="logo"><img height="70" src="${platform.logo}"></img></div>
        <div>
	        <spring:message code="platform.gamesForPlatform" arguments="${platform.name}" var="gamesForPlatform"/>
	        <c:set var="games" value="${platform.games}"/>
	        <c:set var="listName" value="${gamesForPlatform}"/>
	        <%@ include file="gameList.jsp"%>
    	</div>
    </div>
</body>
</html>