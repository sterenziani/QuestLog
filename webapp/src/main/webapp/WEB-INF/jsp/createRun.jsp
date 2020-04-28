<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/game.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetails.css"/>">
   <link rel="stylesheet" type="text/css" href="<c:url value="/css/userForm.css"/>">
</head>
<body>
    <%@ include file="navigation.jsp"%>
	<div class="content">
		<div class="game">
			<div class="run-form-game-title">
				<h2>${game.title}</h2>
			</div>
			<div class="game-details">
				<div class="game-details-cover">
	        		<img src="<c:url value="${game.cover}"/>" alt="<c:out value="${game.title}"/>">
	    		</div>
    		</div>
		    <div class="game-details-runs">
		    	<form:form name="runs" method="POST" action="run/${game.id}">
		    		<input type="hidden" value="${game.id}" name="game"/>
		    		<div class="user-form">
			            <div class="form-field">
			            	<strong><spring:message code="game.platform"/></strong>
			               	<select name="platforms" id="platforms">
			                	<c:forEach var="platform" items="${game.platforms}">
			                    <option value="${platform.name}">${platform.name}</option>
			                    </c:forEach>
							</select>
						</div>
						<div class="form-field">
							<strong><spring:message code="game.playstyle"/></strong>
							<select name="playstyles" id="playstyles">
			                	<c:forEach var="playstyle" items="${playstyles}">
			                    	<option value="${playstyle}"><spring:message code="playstyle.${playstyle.name}"/></option>
			                	</c:forEach>
							</select>
						</div>
						<div class="form-field">
							<strong><spring:message code="game.time"/></strong>
							<input type="number" min="0" max="9999" id="hours" name="hours" value="0"/><strong> :</strong>
							<input type="number" min="0" max="59" id="mins" name="mins" value="0"/><strong> :</strong>
							<input type="number" min="0" max="59" id="secs" name="secs" value="0"/>
						</div>
						<input type="submit" class="button submit-run-button" value="Submit">
					</div>
				</form:form>
			</div>
		</div>
    </div>
</body>
</html>