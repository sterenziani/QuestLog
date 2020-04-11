<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>List of Games</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
</head>
<body>
    <div class="game-list">
        <div class="game-list-header">
            <h2>List Name Placeholder</h2>
        </div>
        <div class="game-list-games">
            <c:forEach var="game" items="${games}">
                <%@ include file="gameListItem.jsp"%>
            </c:forEach>
        </div>
    </div>
</body>
</html>
