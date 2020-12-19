<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
	<%@include file="../common/commonHead.jsp"%>
	<title>QuestLog - <c:out value="${game.title}"/></title>
</head>
<body>
    <%@ include file="../common/navigation.jsp"%>
	<div class="card m-5 bg-very-light right-wave left-wave">
			<div class="card-header bg-very-dark text-white">
				<h2 class="share-tech-mono"><c:out value="${game.title}"/></h2>
			</div>
		<div class="card-body d-flex padding-left-wave padding-right-wave">
			<div>
				<%@include file="gameDetails.jsp"%>
			</div>
			<div class="p-3 col-9">
				<c:choose>
				<c:when test="${interactionEnabled}">
					<%@include file="scoreForm.jsp"%>
					
					<div class="mt-4 bg-very-light pb-5">
						<ul class="nav nav-tabs bg-dark">
						<c:choose>
						<c:when test="${reviewInterest}">
							<spring:message code="game.tabs.runs" var="tabsRun"/>
							<li class="py-2 px-5 mx-auto"><a data-toggle="tab" href="#time-tab"><c:out value="${tabsRun}"/></a></li>
							<c:if test="${!empty loggedUser && !empty user_runs}">
								<spring:message code="game.tabs.myRuns" var="tabsMyRuns"/>
								<li class="py-2 px-5 mx-auto"><a data-toggle="tab" href="#user-run-tab"><c:out value="${tabsMyRuns}"/></a></li>
							</c:if>
							<spring:message code="game.tabs.reviews" var="tabsReviews"/>
							<li class="active py-2 px-5 mx-auto"><a data-toggle="tab" href="#review-tab"><c:out value="${tabsReviews}"/></a></li>
							<c:if test="${!empty loggedUser && !empty userReviews}">
								<spring:message code="game.tabs.myReviews" var="tabsMyReviews"/>
								<li class="py-2 px-5 mx-auto"><a data-toggle="tab" href="#user-review-tab"><c:out value="${tabsMyReviews}"/></a></li>
							</c:if>
						</c:when>
						<c:otherwise>
							<spring:message code="game.tabs.runs" var="tabsRun"/>
							<li class="active py-2 px-5 mx-auto"><a data-toggle="tab" href="#time-tab"><c:out value="${tabsRun}"/></a></li>
							<c:if test="${!empty loggedUser && !empty user_runs}">
								<spring:message code="game.tabs.myRuns" var="tabsMyRuns"/>
								<li class="py-2 px-5 mx-auto"><a data-toggle="tab" href="#user-run-tab"><c:out value="${tabsMyRuns}"/></a></li>
							</c:if>
							<spring:message code="game.tabs.reviews" var="tabsReviews"/>
							<li class="py-2 px-5 mx-auto"><a data-toggle="tab" href="#review-tab"><c:out value="${tabsReviews}"/></a></li>
							<c:if test="${!empty loggedUser && !empty userReviews}">
								<spring:message code="game.tabs.myReviews" var="tabsMyReviews"/>
								<li class="py-2 px-5 mx-auto"><a data-toggle="tab" href="#user-review-tab"><c:out value="${tabsReviews}"/></a></li>
							</c:if>
						</c:otherwise>
						</c:choose>
						</ul>
						
						<c:choose>
						<c:when test="${reviewInterest}">
							<div class="tab-content">
								<div id="time-tab" class="tab-pane fade">
									<div class="col text-center mt-4">
										<spring:message code="game.addRun" var="addRun"/>
										<a class="btn btn-success create-run-button button" href="<c:url value="/createRun/${game.id}"/>"><c:out value="${addRun}"/></a>
			  						</div>
									<%@include file="averageTimes.jsp"%>
									<%@include file="topRuns.jsp"%>
								</div>
			  					
			  					<div id="user-run-tab" class="tab-pane fade">
			  						<div class="col text-center mt-4">
										<spring:message code="game.addRun" var="addRun"/>
										<a class="btn btn-success create-run-button button" href="<c:url value="/createRun/${game.id}"/>"><c:out value="${addRun}"/></a>
			  						</div>
									<c:if test="${loggedUser != null && !empty user_runs}">
										<%@include file="userRuns.jsp"%>
									</c:if>
			  					</div>
		
								<div id="review-tab" class="tab-pane fade show active">
									<div class="col text-center mt-4">
										<spring:message code="review.writeReview" var="writeReview"/>
										<a class="btn btn-success create-run-button button" href="<c:url value="/reviews/create/${game.id}"/>"><c:out value="${writeReview}"/></a>
			  						</div>
									<c:if test="${reviewsCropped}">
										<c:set var="seeAllReviewsUrl" value="/games/${game.id}/reviews"/>
									</c:if>
									<spring:message code="game.reviews" arguments="${game}" var="reviewsListName"/>
									<spring:message code="game.noReviews" var="emptyListMessage"/>
									<%@ include file="../common/reviewsList.jsp"%>
								</div>
								
								<div id="user-review-tab" class="tab-pane fade">
									<div class="col text-center mt-4">
										<spring:message code="review.writeReview" var="writeReview"/>
										<a class="btn btn-success create-run-button button" href="<c:url value="/reviews/create/${game.id}"/>"><c:out value="${writeReview}"/></a>
			  						</div>							
									<c:if test="${!empty loggedUser}">
										<c:if test="${userReviewsCropped}">
											<c:set var="seeAllReviewsUrl" value="/users/${loggedUser.id}/reviews"/>
										</c:if>
										<spring:message code="game.yourReviews" var="reviewsListName"/>
										<spring:message code="game.yourReviews.empty" var="emptyListMessage"/>
										<c:set var="reviewsInPage" value="${userReviews}"/>
										<%@ include file="../common/reviewsList.jsp"%>
									</c:if>
								</div>
							</div>
						</c:when>
						<c:otherwise>
							<div class="tab-content">
								<div id="time-tab" class="tab-pane fade show active">
									<div class="col text-center mt-4">
										<spring:message code="game.addRun" var="addRun"/>
										<a class="btn btn-success create-run-button button" href="<c:url value="/createRun/${game.id}"/>"><c:out value="${addRun}"/></a>
			  						</div>
									<%@include file="averageTimes.jsp"%>
									<%@include file="topRuns.jsp"%>
								</div>
			  					
			  					<div id="user-run-tab" class="tab-pane fade">
			  						<div class="col text-center mt-4">
										<spring:message code="game.addRun" var="addRun"/>
										<a class="btn btn-success create-run-button button" href="<c:url value="/createRun/${game.id}"/>"><c:out value="${addRun}"/></a>
			  						</div>
									<c:if test="${loggedUser != null && !empty user_runs}">
										<%@include file="userRuns.jsp"%>
									</c:if>
			  					</div>
		
								<div id="review-tab" class="tab-pane fade">
									<div class="col text-center mt-4">
										<spring:message code="review.writeReview" var="writeReview"/>
										<a class="btn btn-success create-run-button button" href="<c:url value="/reviews/create/${game.id}"/>"><c:out value="${writeReview}"/></a>
									</div>
									<c:if test="${reviewsCropped}">
										<c:set var="seeAllReviewsUrl" value="/games/${game.id}/reviews"/>
									</c:if>
									<c:set var="listId" value="all-reviews"/>
									<spring:message code="game.reviews" arguments="${game}" var="reviewsListName"/>
									<spring:message code="game.noReviews" var="emptyListMessage"/>
									<%@ include file="../common/reviewsList.jsp"%>
								</div>
								
								<div id="user-review-tab" class="tab-pane fade">
									<div class="col text-center mt-4">
										<spring:message code="review.writeReview" var="writeReview"/>
										<a class="btn btn-success create-run-button button" href="<c:url value="/reviews/create/${game.id}"/>"><c:out value="${writeReview}"/></a>
			  						</div>							
									<c:if test="${!empty loggedUser}">
										<c:if test="${userReviewsCropped}">
											<c:set var="seeAllReviewsUrl" value="/users/${loggedUser.id}/reviews"/>
										</c:if>
										<spring:message code="game.yourReviews" var="reviewsListName"/>
										<c:set var="listId" value="your-reviews"/>
										<spring:message code="game.yourReviews.empty" var="emptyListMessage"/>
										<c:set var="reviewsInPage" value="${userReviews}"/>
										<%@ include file="../common/reviewsList.jsp"%>
									</c:if>
								</div>
							</div>

						</c:otherwise>
						</c:choose>
					</div>
				</c:when>
				<c:otherwise>
				    <div class="container text-center align-middle">
				    	<div class="my-5 py-5 bg-light border-bottom border-primary rounded-lg">
							<spring:message code="error.interactionDisabled" var="interactionDisabled"/>
					        <h5 class="align-middle"><c:out value="${interactionDisabled}"/></h5>
				        </div>
				    </div>
				</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>

</body>
</html>

<script type="text/javascript" src="<c:url value="/js/game-forms/game-score.js"/>"></script>