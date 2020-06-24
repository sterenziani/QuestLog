<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <c:out value="${user.username}"/></title>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
	    <div class="mx-5 align-middle ">
			<div class="mb-4 mt-5 text-center">
				<h1 class="align-middle share-tech-mono"><c:out value="${user.username}"/></h1>
				<c:if test="${loggedUser.id == user.id}">
					<h5 class="align-middle">(<c:out value="${user.email}"/>)</h5>
				</c:if>
			</div>
			<div class="d-flex text-left flex-wrap">
				<div class="mb-0 m-3 bg-dark border-bottom border-primary rounded-lg text-white flex-grow-1 d-flex justify-content-center">
					<div class="pl-3 d-flex justify-content-center flex-column">
						<i class="fa fa-4x fa-star d-block"></i>
					</div>
					<div class="pt-5 pb-5 pr-5 pl-3">
						<h5><spring:message code="user.gamesRated" arguments="${user.scoreCount}"/></h5>
						<h5><spring:message code="user.averageScore" arguments="${user.scoreAverage}"/></h5>
					</div>
				</div>
				<div class="mb-0 m-3 bg-dark border-bottom border-primary rounded-lg text-white flex-grow-1 d-flex justify-content-center">
					<div class="pl-3 d-flex justify-content-center flex-column">
						<i class="fa fa-4x fa-gamepad d-block"></i>
					</div>
					<div class="pt-5 pb-5 pr-5 pl-3">
						<h5><spring:message code="user.createdRuns" arguments="${user.runCount}"/></h5>
						<h5><spring:message code="user.hoursPlayed" arguments="${user.totalHoursPlayed}"/></h5>
					</div>
				</div>
				<c:if test="${user.favoriteGame != null}">
					<div class="mb-0 m-3 py-3 px-5 bg-dark border-bottom border-primary rounded-lg text-white flex-grow-1 d-flex justify-content-center align-items-center">
						<c:set var="favGame" value="${user.favoriteGame.title}"/>
						<h5 class="pr-3"><spring:message code="user.favoriteGame" arguments="${favGame}"/></h5>
						<a href="<c:url value="/games/${user.favoriteGame.id}"/>" class="text-white">
							<div class="bg-primary d-flex flex-row align-items-center">
								<img width="100px" src="<c:url value="/images/${user.favoriteGame.cover}"/>" alt="${favGame}"/>
							</div>
						</a>
					</div>
				</c:if>
			</div>
	    </div>
		<div class="m-5 bg-very-light right-wave left-wave pb-1">
			<ul class="nav nav-tabs bg-dark">
				<li class="active py-2 px-5 mx-auto"><a data-toggle="tab" href="#user-backlog"><spring:message code="game.details.backlog"/></a></li>
				<li class="py-2 px-5 mx-auto"><a data-toggle="tab" href="#user-scores"><spring:message code="game.details.scores"/></a></li>
				<li class="py-2 px-5 mx-auto"><a data-toggle="tab" href="#user-runs"><spring:message code="game.details.runs"/></a></li>
				<li class="py-2 px-5 mx-auto"><a data-toggle="tab" href="#user-reviews"><spring:message code="game.details.reviews"/></a></li>
			</ul>
			<div class="tab-content">
				<div id="user-backlog" class="main-game-lists-backlog tab-pane fade show active">
					<spring:message code="user.backlog" arguments="${user.username}" var="myBacklog"/>
					<c:set var="listName" value="${myBacklog}"/>
					<c:set var="games" value="${backlog}"/>
					<c:if test="${backlogCropped}">
						<c:set var="seeAllUrl" value="/backlog/${user.id}"/>
					</c:if>
					<%@ include file="../common/gameList.jsp"%>
					<c:remove var="seeAllUrl"/>
				</div>
				<div id="user-scores" class="tab-pane fade">
					<c:set var="scoresInPage" value="${scoresInPage}"/>
					<c:if test="${scoresCropped}">
						<c:set var="seeAllScoresUrl" value="/users/${user.id}/scores"/>
					</c:if>
					<%@ include file="../common/scoresList.jsp"%>
				</div>
				<div id="user-runs" class="tab-pane fade">
					<c:set var="runsInPage" value="${runsInPage}"/>
					<c:if test="${runsCropped}">
						<c:set var="seeAllRunsUrl" value="/users/${user.id}/runs"/>
					</c:if>
					<%@ include file="../common/runsList.jsp"%>
				</div>
				<div id="user-reviews" class="tab-pane fade">
					<c:set var="reviewsInPage" value="${reviewsInPage}"/>
					<c:if test="${reviewsCropped}">
						<c:set var="seeAllReviewsUrl" value="/users/${user.id}/reviews"/>
					</c:if>
					<spring:message code="user.reviews" arguments="${user.username}" var="reviewsListName"/>
					<spring:message code="user.noReviews" var="emptyListMessage"/>
					<%@ include file="../common/reviewsList.jsp"%>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
