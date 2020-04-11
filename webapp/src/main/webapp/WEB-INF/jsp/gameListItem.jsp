<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: paula
  Date: 11/4/20
  Time: 12:55
  To change this template use File | Settings | File Templates.
--%>
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
