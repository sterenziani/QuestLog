<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
	<spring:message code="game.addRun" var="title"/>
    <title>QuestLog - <c:out value="${title}"/></title>
</head>
<body>
    <%@ include file="../common/navigation.jsp"%>
	<div class="card m-5 bg-very-light right-wave left-wave">
		<div class="card-header bg-very-dark text-white">
			<spring:message code="game.addingRun" arguments="${game.title}" var="addingRun"/>
			<h2 class="share-tech-mono"><c:out value="${addingRun}"/></h2>
		</div>
		<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
		
			<div class="game-details">
				<div class="game-details-cover">
	        		<img class="cover" src="<c:url value="/images/${game.cover}"/>" alt="<c:out value="${game.title}"/>">
	    		</div>
    		</div>
    	</div>	
    	
    	<div class="card-body d-flex flex-wrap  padding-left-wave justify-content-center padding-right-wave">
		    <div class="game-details-runs">
		    	<form:form name="runs" method="POST" action="run/${game.id}">
		    		<input type="hidden" value="<c:out value="${game.id}"/>" name="game"/>
		    		<input id="removeFromBacklogInput" type="hidden" value="false" name="removeFromBacklog"/>
		    		<div class="user-form" style="text-align: center; vertical-align: middle;">
			            <div class="form-field mt-2">
							<spring:message code="game.platform" var="titlePlatform"/>
			            	<h5><strong><c:out value="${titlePlatform}"/></strong></h5>
			            	<div class="form-group">
				               	<select class="form-control" name="platforms" id="platforms" style="padding: 5px">
				                	<c:forEach var="platform" items="${game.platforms}">
				                    	<option value="<c:out value="${platform.name}"/>"><c:out value="${platform.name}"/></option>
				                    </c:forEach>
								</select>
			            	</div>
						</div>
						<div class="form-field mt-2">
							<spring:message code="game.playstyle" var="titlePlaystyle"/>
							<h5><strong><c:out value="${titlePlaystyle}"/></strong></h5>
							<div class="form-group">
								<select class="form-control" name="playstyles" id="playstyles" style="padding: 5px">
				                	<c:forEach var="playstyle" items="${playstyles}">
										<spring:message code="playstyle.${playstyle.name}" var="playstyleName"/>
				                    	<option value="<c:out value="${playstyle}"/>"><c:out value="${playstyleName}"/></option>
				                	</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-field mt-2">
							<spring:message code="game.time" var="time"/>
							<h5><strong><c:out value="${time}"/></strong></h5>
							<input type="number" min="0" max="9999" id="hours" name="hours" value="0"/><strong> :</strong>
							<input type="number" min="0" max="59" id="mins" name="mins" value="0"/><strong> :</strong>
							<input type="number" min="0" max="59" id="secs" name="secs" value="0"/>
						</div>
			            <c:choose>
			                <c:when test="${game.inBacklog}">
								<spring:message code="game.submit" var="gameSubmit"/>
								<input type="button" class="btn btn-dark mt-3" value="<c:out value="${gameSubmit}"/>" data-toggle="modal" data-target="#removeGameFromBacklogModal-<c:out value="${game.id}"/>"/>

					            <div class="modal fade" id="removeGameFromBacklogModal-<c:out value="${game.id}"/>" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
					                <div class="modal-dialog modal-dialog-centered" role="document">
					                    <div class="modal-content">
					                        <div class="modal-header">
												<spring:message code="game.removeFromBacklogAsk" arguments="${game.title}" var="removeFromBacklogAsk"/>
					                            <h5 class="modal-title" id="removeGameFromBacklogLabel-<c:out value="${game.id}"/>"><c:out value="${removeFromBacklogAsk}"/></h5>
					                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
					                                <span aria-hidden="true">&times;</span>
					                            </button>
					                        </div>
					                        <div class="modal-body">
					                            <spring:message code="game.removeFromBacklogExplain"/>
					                        </div>
					                        <div class="modal-footer">
												<spring:message code="game.postButKeepInBacklog" var="keep"/>
					                            <button type="button" id="postButKeepInBacklogButton" class="btn btn-light" data-dismiss="modal"><c:out value="${keep}"/></button>
												<spring:message code="game.postAndRemoveFromBacklog" var="remove"/>
					                            <a id="postAndRemoveFromBacklogButton" class="btn btn-primary text-white"><c:out value="${remove}"/></a>
					                        </div>
					                    </div>
					                </div>
					            </div>
			                </c:when>
			                <c:otherwise>
								<spring:message code="game.submit" var="gameSubmit"/>
								<input type="submit" class="btn btn-dark mt-3" value="<c:out value="${gameSubmit}"/>"/>
			                </c:otherwise>
			            </c:choose>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>

<script type="text/javascript" src="<c:url value="/js/game-forms/game-run.js"/>"></script>