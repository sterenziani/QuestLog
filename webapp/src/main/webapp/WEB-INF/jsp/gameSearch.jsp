<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
</head>
<body class="background-primary">
<%@include file="navigation.jsp"%>
<c:url value="/searchFilter" var="path"/>
<div class="content">
	<div class="search-parameters">
	<form:form name="searchFilter" method="GET" action="${path}" >
		<input type="hidden" value="${searchTerm}" name="searchTerm"/>
		<strong><spring:message code="game.platform"/></strong>
			<select name="platforms" id="platforms" multiple>
			<c:forEach var="platform" items="${platforms}">
				<option value="${platform.id}">${platform.name}</option>
			</c:forEach>
			</select>
		<strong><spring:message code="game.genres"/></strong>
			<select name="genres" id="genres" multiple>
			<c:forEach var="genre" items="${genres}">
				<option value="${genre.id}">${genre.name}</option>
			</c:forEach>
			</select>
		<div>
		<strong><spring:message code="game.score"/></strong>
			<input type="number" min="0" max="100" id="scoreLeft" name="scoreLeft" value="0"/><strong> - </strong>
			<input type="number" min="0" max="100" id="scoreRight" name="scoreRight" value="100"/>
		</div>
		<div>
		<strong><spring:message code="game.timeSearch"/></strong>
			<input type="number" min="0" max="9999" id="hoursLeft" name="hoursLeft" value="0"/><strong> :</strong>
			<input type="number" min="0" max="59" id="minsLeft" name="minsLeft" value="0"/><strong> :</strong>
			<input type="number" min="0" max="59" id="secsLeft" name="secsLeft" value="0"/><strong> - </strong>
			<input type="number" min="0" max="9999" id="hoursRight" name="hoursRight" value="9999"/><strong> :</strong>
			<input type="number" min="0" max="59" id="minsRight" name="minsRight" value="59"/><strong> :</strong>
			<input type="number" min="0" max="59" id="secsRight" name="secsRight" value="59"/>
		</div>
		<input type="submit" class="filter-search-button" value="<spring:message code="game.filter"/>">
	</form:form>
	</div>
	
    <div class="game-search-results">
        <c:set var="listName"><spring:message code="search.results" arguments="${searchTerm}"/></c:set>
        <%@ include file="gameList.jsp"%>
    </div>
</div>
</body>
</html>