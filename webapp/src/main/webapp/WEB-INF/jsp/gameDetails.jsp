<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${game.title}</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetails.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetailsInfoItem.css"/>">
</head>
<body>
    <div class="game-details">
        <div class="game-details-cover">
            <img src="<c:url value="${game.cover}"/>" alt="${game.title}">
        </div>
        <div class="game-details-content">
            <div class="game-details-description">
                <p>${game.description}</p>
            </div>
            <div class="game-details-info">
                <dl>
                    <div class="game-details-release-dates">
                        <c:set var="title" value="Release Dates"/>
                        <c:set var="items" value="${game.releaseDates}"/>
                        <%@include file="gameDetailsInfoItem.jsp"%>
                    </div>
                    <div class="game-details-genres">
                        <c:set var="title" value="Genres"/>
                        <c:set var="items" value="${game.genres}"/>
                        <%@include file="gameDetailsInfoItem.jsp"%>
                    </div>
                    <div class="game-details-platforms">
                        <c:set var="title" value="Platforms"/>
                        <c:set var="items" value="${game.platforms}"/>
                        <%@include file="gameDetailsInfoItem.jsp"%>
                    </div>
                    <div class="game-details-developers">
                        <c:set var="title" value="Developers"/>
                        <c:set var="items" value="${game.developers}"/>
                        <%@include file="gameDetailsInfoItem.jsp"%>
                    </div>
                    <div class="game-details-publishers">
                        <c:set var="title" value="Publishers"/>
                        <c:set var="items" value="${game.publishers}"/>
                        <%@include file="gameDetailsInfoItem.jsp"%>
                    </div>
                </dl>
            </div>
        </div>
    </div>
</body>
</html>
