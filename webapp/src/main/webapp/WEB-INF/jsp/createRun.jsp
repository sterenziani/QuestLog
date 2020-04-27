<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/game.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetails.css"/>">
</head>
<body>
    <%@ include file="navigation.jsp"%>
    <div class="content">
        <div class="game-title">
            <h2>${game.title}</h2>
        </div>
            <div class="game-details-cover">
        <img src="<c:url value="${game.cover}"/>" alt="<c:out value="${game.title}"/>">
    </div>
    <div class="game-details-runs">
        <form:form name="runs" method="POST" action="run/${game.id}">
              <input type="hidden" value="${game.id}" name="game"/>
              <div>
              <strong><spring:message code="game.platforms"/></strong>
               <select name="platforms" id="platforms">
                	<c:forEach var="platform" items="${game.platforms}">
                    <option value="${platform.name}">${platform.name}</option>
                    </c:forEach>
				</select>
				</div>				
				<div>
				<strong><spring:message code="game.playstyle"/></strong>
				<select name="playstyles" id="playstyles">
                	<c:forEach var="playstyle" items="${playstyles}">
                    <option value="${playstyle}"><spring:message code="playstyle.${playstyle.name}"/></option>
                </c:forEach>
				</select>
				</div>
				<div>
				<strong><spring:message code="game.time"/></strong>
					<input type="number" min="0" max="10000" id="hours" name="hours" value="0">:</input>
					<input type="number" min="0" max="59" id="mins" name="mins" value="0">:</input>
					<input type="number" min="0" max="59" id="secs" name="secs" value="0">
				</div>
				<input type="submit" value="Submit">
		</form:form>
    </div> 
    </div>
</body>
</html>