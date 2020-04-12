<%--
    Include this page:
        <%@ include file="gameListItem.jsp"%>

    Including jsp should have:
        * variable game
        * inside the header:
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">

--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="game-list-item">
    <div class="game-list-item-cover">
        <a href="<c:url value="/games/${game.id}"/>"><img src="<c:url value="${game.cover}"/>" alt="${game.title}"/></a>
    </div>
    <div class="game-list-item-data">
        <h3>Title</h3>
    </div>
</div>
