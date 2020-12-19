<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
	<spring:message code="review.writeReview" var="title"/>
	 <title>QuestLog - <c:out value="${title}"/></title>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="card m-5 bg-very-light right-wave left-wave">
        <div class="card-header bg-very-dark text-white d-flex">
            <div>
                <h2 class="share-tech-mono">
                	<spring:message code="review.forGame" arguments="${game.title}" var="forGame"/>
					<c:out value="${forGame}"/>
                </h2>
            </div>
        </div>
        <div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
            <form:form modelAttribute="reviewForm" method="post" enctype="multipart/form-data" cssClass="w-100">
                
				<div class="form-group">
					<div class="row">
						<div class="col-9 my-auto">
								<div class="row m-auto">
									<spring:message code="game.yourScore" var="yourScore"/>
									<form:label for="score" path="score"><strong><c:out value="${yourScore}"/></strong></form:label>
								</div>
								<spring:message code="game.yourScore" var="score"/>
								<c:choose>
									<c:when test="${empty user_score}">
										<div class="score-slider">
											<input class="slider mt-3 mb-3" id="range-slider" type="range" name="score" min="0" max="100" oninput="scoreText.innerHTML = document.getElementById('range-slider').value" value="50">
										</div>
									</c:when>
									<c:otherwise>
										<div class="score-slider">
											<input class="slider mt-3 mb-3" id="range-slider" type="range" name="score" min="0" max="100" oninput="scoreText.innerHTML = document.getElementById('range-slider').value" value="${user_score.score}">
										</div>
									</c:otherwise>
								</c:choose>
								<input type="hidden" value="<c:out value="${game.id}"/>" name="game"/>
						</div>
						<div class="col-3 mt-5">
							<c:choose>
								<c:when test="${empty user_score}">
									<div class="text-center px-5"><div class="score-number">
										<p class="display-4 score-display badge badge-success" id="scoreText">50</p>
									</div></div>
								</c:when>
								<c:otherwise>
									<div class="text-center px-5"><div class="score-number">
										<p class="score-display badge badge-success" id="scoreText"><c:out value="${user_score.score}"/></p>
									</div></div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
                
                <div class="form-group">
					<spring:message code="game.platform" var="platformTitle"/>
                    <form:label for="platform" path="platform"><strong><c:out value="${platformTitle}"/></strong></form:label>
	               	<select class="form-control" name="platform" id="platforms" style="padding: 5px">
	                	<c:forEach var="element" items="${game.platforms}">
	                    	<option value="<c:out value="${element.id}"/>"><c:out value="${element.name}"/></option>
	                    </c:forEach>
					</select>
                </div>
                
                <div class="form-group">
                	<form:errors path="body" class="form-error" element="p"/>
					<spring:message code="review.body" var="reviewBody"/>
                    <form:label for="body" path="body"><strong><c:out value="${reviewBody}"/></strong></form:label>
                    <form:textarea cssClass="form-control" rows="8" path="body" name="body" type="text"/>
                </div>
				<c:choose>
					<c:when test="${!game.inBacklog}">
						<button type="submit" class="btn btn-primary btn-block mt-5">
							<spring:message code="review.submit" var="submit"/>
							<c:out value="${submit}"/>
						</button>
					</c:when>
					<c:otherwise>
						<button type="button" class="btn btn-primary btn-block mt-5" data-toggle="modal" data-target="#removeFromBacklogBecauseReview">
							<spring:message code="review.submit" var="submit"/>
							<c:out value="${submit}"/>
						</button>
						<div class="modal fade" id="removeFromBacklogBecauseReview" tabindex="-1" role="dialog" aria-labelledby="removeFromBacklogBecauseReviewModalLabel" aria-hidden="true">
							<div class="modal-dialog modal-dialog-centered" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<spring:message code="review.create" var="reviewCreate"/>
										<h5 class="modal-title" id="removeFromBacklogBecauseReviewModalLabel"><c:out value="${reviewCreate}"/></h5>
										<button type="button" class="close" data-dismiss="modal" aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
									</div>
									<div class="modal-body">
										<spring:message code="review.create.message" var="createMessage"/>
										<c:out value="${createMessage}"/>
									</div>
									<div class="modal-footer">
										<c:url value="/reviews/create/${game.id}" var="post_url"/>
										<spring:message code="game.removeFromBacklog" var="removeFromBacklog"/>
										<form:input type="hidden" path="removeFromBacklog" id="removeFromBacklog" value="${false}"/>
										<spring:message code="review.create.dismiss" var="createDismiss"/>
										<button type="submit" class="btn btn-light"><c:out value="${createDismiss}"/></button>

										<spring:message code="game.removeFromBacklog" var="removeFromBacklog"/>
										<input class="btn btn-danger" type="submit" onclick="document.getElementById('removeFromBacklog').value = true" value="<c:out value="${removeFromBacklog}"/>"/>
									</div>
								</div>
							</div>
						</div>
					</c:otherwise>
				</c:choose>
            </form:form>
        </div>
    </div>
</body>
</html>
