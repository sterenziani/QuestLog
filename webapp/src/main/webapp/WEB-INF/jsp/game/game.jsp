<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
			<div class="p-3 flex-grow-1">
				
			<div class="container">	
			<div class="row">
				<div class="col-sm-3">
					<div class="row"><strong class="score-title"><spring:message code="game.averageUserScore"/></strong></div>
					<div class="row">
						<c:choose>
							<c:when test="${empty averageScore}">
								<p class="score-display badge badge-dark score-display-avg"><spring:message code="game.notAvailable"/></p>
							</c:when>
							<c:otherwise>
								<p class="score-display badge badge-dark"><c:out value="${averageScore}"/></p>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<div class="col-sm-9">
				<form:form name="scores" method="POST" action="scores/${game.id}">
					<div class="row">
						<div class="col-sm-7 my-auto">
								<input id="removeFromBacklogInput" type="hidden" value="false" name="removeFromBacklog"/>
								<div class="row m-auto">
									<strong class="score-title"><spring:message code="game.yourScore"/></strong>
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
						<div class="col">
							<c:choose>
								<c:when test="${empty user_score}">
									<div class="text-center px-5"><div class="score-number">
										<p class="display-4 score-display badge badge-success" id="scoreText">-</p>
									</div></div>
								</c:when>
								<c:otherwise>
									<div class="text-center px-5"><div class="score-number">
										<p class="score-display badge badge-success" id="scoreText"><c:out value="${user_score.score}"/></p>
									</div></div>
								</c:otherwise>
							</c:choose>
							<div class="score-submit px-5">
					            <c:choose>
					                <c:when test="${game.inBacklog && loggedUser != null}">
										<input type="button" class="btn btn-primary btn-block score-submit-button button" value="<spring:message code="game.rate"/>" data-toggle="modal" data-target="#removeGameFromBacklogModal-${game.id}"/>
							            
							            <div class="modal fade" id="removeGameFromBacklogModal-${game.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
							                <div class="modal-dialog modal-dialog-centered" role="document">
							                    <div class="modal-content">
							                        <div class="modal-header">
							                            <h5 class="modal-title" id="removeGameFromBacklogLabel-${game.id}"><spring:message code="game.removeFromBacklogAsk" arguments="${game.title}"/></h5>
							                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
							                                <span aria-hidden="true">&times;</span>
							                            </button>
							                        </div>
							                        <div class="modal-body">
							                            <spring:message code="game.removeFromBacklogExplain"/>
							                        </div>
							                        <div class="modal-footer">
							                            <button type="button" id="postButKeepInBacklogButton" class="btn btn-light" data-dismiss="modal"><spring:message code="game.postButKeepInBacklog"/></button>
							                            <a id="postAndRemoveFromBacklogButton" class="btn btn-primary text-white"><spring:message code="game.postAndRemoveFromBacklog"/></a>
							                        </div>
							                    </div>
							                </div>
							            </div>
					                </c:when>
					                <c:otherwise>
										<input type="submit" class="btn btn-primary btn-block score-submit-button button" value="<spring:message code="game.rate"/>"/>
					                </c:otherwise>
					            </c:choose>
							</div>
						</div>
					</div>
				</form:form>
				</div>
			</div>
			</div>

			<c:choose>
				<c:when test="${runsEnabled}">
					<div class="card m-5 bg-very-light right-wave left-wave">
					    <div class="card-header bg-very-dark text-white d-flex">
					    	<div>
					            <h2 class="share-tech-mono"><spring:message code="game.averageTime"/></h2>
					        </div>
						</div>
						<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
							<div class="container">
								<div class="row">
									<div class="col text-right bg-primary text-white"><strong><spring:message code="game.playstyle"/></strong></div>
									<div class="col bg-primary text-white"><strong><spring:message code="game.averageTime"/></strong></div>
								</div>
								<c:forEach var="element" items="${playAverage}">
									<div class="row">
										<div class="col text-right"><spring:message code="playstyle.${element.key}"/></div>
										<div class="col">
											<c:if test="${element.value != '0 : 00 : 00'}">
												<c:out value="${element.value}"/>
											</c:if>
											<c:if test="${element.value == '0 : 00 : 00'}">
												<spring:message code="game.notAvailable"/>
											</c:if>
										</div>
									</div>
								</c:forEach>
							</div>
						</div>
					</div>
					
					<div class="card m-5 bg-very-light right-wave left-wave">
					    <div class="card-header bg-very-dark text-white d-flex">
					    	<div>
					            <h2 class="share-tech-mono"><spring:message code="game.fastestRuns"/></h2>
					        </div>
						</div>
						<c:if test="${!empty topRuns}">
							<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
								<div class="container">
									<c:forEach var="element" items="${topRuns}">
										<div class="row">
											<div class="col text-right"><a href="<c:url value="/users/${element.user.id}"/>"><c:out value="${element.user}"/></a></div>
											<div class="col text-center"><c:out value="${element.platform.shortName}"/></div>
											<div class="col text-center"><c:out value="${element}"/></div>
										</div>
									</c:forEach>
								</div>
							</div>
						</c:if>
						<c:if test="${empty topRuns}">
							<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
								<div class="container text-center">
									<spring:message code="game.noRuns"/>
								</div>
							</div>
						</c:if>
					</div>
					
					<c:if test="${loggedUser != null && !empty user_runs}">
						<div class="card m-5 bg-very-light right-wave left-wave">
						    <div class="card-header bg-very-dark text-white d-flex">
						    	<div>
						            <h2 class="share-tech-mono"><spring:message code="game.yourRuns"/></h2>
						        </div>
							</div>
							<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
								<div class="container">
									<div class="row">
										<div class="col text-center bg-primary text-white"><strong><spring:message code="game.platform"/></strong></div>
										<div class="col text-center bg-primary text-white"><strong><spring:message code="game.playstyle"/></strong></div>
										<div class="col text-center bg-primary text-white"><strong><spring:message code="game.yourTime"/></strong></div>
									</div>
									<c:forEach var="element" items="${user_runs}">
										<div class="row">
											<div class="col text-center"><c:out value="${element.platform.shortName}"/></div>
											<div class="col text-center"><spring:message code="playstyle.${element.playstyle.name}"/></div>
											<div class="col text-center"><c:out value="${element}"/></div>
										</div>
									</c:forEach>
								</div>
							</div>
						</div>
					</c:if>
					<a class="btn btn-primary btn-block create-run-button button" href="<c:url value="/createRun/${game.id}"/>"><spring:message code="game.addRun"/></a>
				</c:when>
				<c:otherwise>
					<div class="card m-5 bg-very-light right-wave left-wave">
					    <div class="card-header bg-very-dark text-white d-flex">
					    	<div>
					            <h2 class="share-tech-mono"><spring:message code="game.averageTime"/></h2>
					        </div>
						</div>
						<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
							<div class="container text-center">
								<spring:message code="error.runNotAvailable"/>
							</div>
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