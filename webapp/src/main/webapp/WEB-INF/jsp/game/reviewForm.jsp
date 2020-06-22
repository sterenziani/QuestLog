<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
	 <title>QuestLog - <spring:message code="review.writeReview"/></title>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="card m-5 bg-very-light right-wave left-wave">
        <div class="card-header bg-very-dark text-white d-flex">
            <div>
                <h2 class="share-tech-mono">
                	<spring:message code="review.forGame" arguments="${game.title}"/>
                </h2>
            </div>
        </div>
        <div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
            <form:form modelAttribute="reviewForm" method="post" enctype="multipart/form-data" cssClass="w-100">
                
				<div class="form-group">
					<div class="row">
						<div class="col-9 my-auto">
								<div class="row m-auto">
									<form:label for="score" path="score"><strong><spring:message code="game.yourScore"/></strong></form:label>
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
								<input type="hidden" value="${game.id}" name="game"/>
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
                    <form:label for="platform" path="platform"><strong><spring:message code="game.platform"/></strong></form:label>
	               	<select class="form-control" name="platform" id="platforms" style="padding: 5px">
	                	<c:forEach var="element" items="${game.platforms}">
	                    	<option value="${element.id}"><c:out value="${element.name}"/></option>
	                    </c:forEach>
					</select>
                </div>
                
                <div class="form-group">
                	<form:errors path="body" class="form-error" element="p"/>
                    <form:label for="body" path="body"><strong><spring:message code="review.body"/></strong></form:label>
                    <form:textarea cssClass="form-control" rows="8" path="body" name="body" type="text"/>
                </div>

				<input type="submit" class="btn btn-primary btn-block mt-5" value="<spring:message code="review.submit"/>"/>

            </form:form>
        </div>
    </div>
</body>
</html>
