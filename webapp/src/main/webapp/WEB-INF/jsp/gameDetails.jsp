<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${game.title}</title>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetails.css"/>">
</head>
<body>
    <div class="game-details">
        <div class="game-details-cover">
            <img src="<c:url value="${game.cover}"/>">
        </div>
        <div class="game-details-content">
            <div class="game-details-description">
                <p>${game.description}</p>
            </div>
            <div class="game-details-info">
                <dl>
                    <div class="game-details-release-dates">
                        <dt><strong>Release Dates</strong></dt>
                        <dd>
                            <ul>
                               <c:forEach var="releaseDate" items="${game.releaseDates}">
                                   <li>${releaseDate}</li>
                               </c:forEach>
                            </ul>
                        </dd>
                    </div>
                    <div class="game-details-genres">
                        <dt><strong>Genres</strong></dt>
                        <dd>
                            <ul>
                                <c:forEach var="genre" items="${game.genres}">
                                    <li>${genre}</li>
                                </c:forEach>
                            </ul>
                        </dd>
                    </div>
                    <div class="game-details-platforms">
                        <dt><strong>Platforms</strong></dt>
                        <dd>
                            <ul>
                                <c:forEach var="platform" items="${game.platforms}">
                                    <li>${platform}</li>
                                </c:forEach>
                            </ul>
                        </dd>
                    </div>
                    <div class="game-details-developers">
                        <dt><strong>Developers</strong></dt>
                        <dd>
                            <ul>
                                <c:forEach var="developer" items="${game.developers}">
                                    <li>${developer}</li>
                                </c:forEach>
                            </ul>
                        </dd>
                    </div>
                    <div class="game-details-publishers">
                        <dt><strong>Publishers</strong></dt>
                        <dd>
                            <ul>
                                <c:forEach var="publisher" items="${game.publishers}">
                                    <li>${publisher}</li>
                                </c:forEach>
                            </ul>
                        </dd>
                    </div>
                </dl>
            </div>
        </div>
    </div>
</body>
</html>
