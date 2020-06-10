<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <spring:message code="game.addRun"/></title>
</head>
<body>
    <%@ include file="../common/navigation.jsp"%>
	<div class="card m-5 bg-very-light right-wave left-wave">
		<div class="card-header bg-very-dark text-white">
			<h2 class="share-tech-mono"><spring:message code="game.addingRun" arguments="${game.title}"/></h2>
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
		    		<input type="hidden" value="${game.id}" name="game"/>
		    		<input id="removeFromBacklogInput" type="hidden" value="false" name="removeFromBacklog"/>
		    		<div class="user-form" style="text-align: center; vertical-align: middle;">
			            <div class="form-field mt-2">
			            	<h5><strong><spring:message code="game.platform"/></strong></h5>
			            	<div class="form-group">
				               	<select class="form-control" name="platforms" id="platforms" style="padding: 5px">
				                	<c:forEach var="platform" items="${game.platforms}">
				                    	<option value="${platform.name}"><c:out value="${platform.name}"/></option>
				                    </c:forEach>
								</select>
			            	</div>
						</div>
						<div class="form-field mt-2">
							<h5><strong><spring:message code="game.playstyle"/></strong></h5>
							<div class="form-group">
								<select class="form-control" name="playstyles" id="playstyles" style="padding: 5px">
				                	<c:forEach var="playstyle" items="${playstyles}">
				                    	<option value="${playstyle}"><spring:message code="playstyle.${playstyle.name}"/></option>
				                	</c:forEach>
								</select>
							</div>
						</div>
						<div class="form-field mt-2">
							<h5><strong><spring:message code="game.time"/></strong></h5>
							<input type="number" min="0" max="9999" id="hours" name="hours" value="0"/><strong> :</strong>
							<input type="number" min="0" max="59" id="mins" name="mins" value="0"/><strong> :</strong>
							<input type="number" min="0" max="59" id="secs" name="secs" value="0"/>
						</div>
			            <c:choose>
			                <c:when test="${game.inBacklog}">
								<input type="button" class="btn btn-dark mt-3" value="<spring:message code="game.submit"/>" data-toggle="modal" data-target="#removeGameFromBacklogModal-${game.id}"/>

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
								<input type="submit" class="btn btn-dark mt-3" value="<spring:message code="game.submit"/>">
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