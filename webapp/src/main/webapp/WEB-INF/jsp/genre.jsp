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
    	<div class="logo"><img height="100" src="${genre.logo}"></img></div>
        <div>
        	<spring:message code="genres.${genre.name}" var="genreName"/>
	        <spring:message code="genre.gamesOfGenre" arguments="${genreName}" var="gamesOfGenre"/>
	        <c:set var="games" value="${genre.games}"/>
	        <c:set var="listName" value="${gamesOfGenre}"/>
	        <%@ include file="gameList.jsp"%>
    	</div>
    </div>
</body>
</html>