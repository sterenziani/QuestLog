<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>QuestLog</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
</head>
<body>
    <div class="main-game-lists-backlog">
        <%@ include file="gameList.jsp"%>
    </div>
    <div class="main-game-lists-recommended">
        <%@ include file="gameList.jsp"%>
    </div>
    <div class="main-game-lists-upcomming">
        <%@ include file="gameList.jsp"%>
    </div>
</body>
</html>
