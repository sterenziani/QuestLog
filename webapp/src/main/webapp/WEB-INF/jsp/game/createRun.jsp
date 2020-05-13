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
	        		<img src="<c:url value="/images/${game.cover}"/>" alt="<c:out value="${game.title}"/>">
	    		</div>
    		</div>
    		</div>
    		<div class="card-body d-flex flex-wrap  padding-left-wave justify-content-center padding-right-wave">
		    <div class="game-details-runs">
		    	<form:form name="runs" method="POST" action="run/${game.id}">
		    		<input type="hidden" value="${game.id}" name="game"/>
		    		<div class="user-form" style="text-align: center; vertical-align: middle;">
			            <div class="form-field mt-2">
			            	<h5><strong><spring:message code="game.platform"/></strong></h5>
			            	<div class="form-group">
				               	<select class="form-control" name="platforms" id="platforms" style="padding: 5px">
				                	<c:forEach var="platform" items="${game.platforms}">
				                    	<option value="${platform.name}">${platform.name}</option>
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
						<input type="submit" class="btn btn-dark mt-3" value="<spring:message code="game.submit"/>">
					</div>
				</form:form>
			</div>
		</div>
		</div>
</body>
</html>