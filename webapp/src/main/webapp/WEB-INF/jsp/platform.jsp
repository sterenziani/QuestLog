<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>QuestLog</title>
    <link rel="stylesheet" type="text/css" href="<c:out value="http://fonts.googleapis.com/css?family=Roboto"/>" >
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigation.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigationSearchBar.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
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