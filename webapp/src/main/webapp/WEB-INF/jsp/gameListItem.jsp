<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
</head>
<body>
    <div class="game-list-item">
        <div class="game-list-item-cover">
            <img src="<c:url value="${game.cover}"/>" alt="${game.title}"/>
        </div>
        <div class="game-list-item-data">
            Title
        </div>
    </div>
</body>
</html>
