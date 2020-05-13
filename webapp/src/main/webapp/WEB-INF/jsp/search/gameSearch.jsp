<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
	<c:choose>
		<c:when test="${empty searchTerm}">
			<title>QuestLog - <spring:message code="search.results" arguments="*"/></title>
		</c:when>
		<c:otherwise>
			<title>QuestLog - <spring:message code="search.results" arguments="${searchTerm}"/></title>
		</c:otherwise>
    </c:choose>
</head>
<body class="background-primary">
<c:set var="gameSearchResults" value="true"/>
<%@include file="../common/navigation.jsp"%>
<c:url value="/search" var="path"/>
<div class="content">
	<div class="search-parameters">
	    <div class="container text-center align-middle">
    	<div class="my-5 py-3 bg-light border-bottom border-primary rounded-lg">
    		<h2 class="share-tech-mono"><spring:message code="search.filters"/></h1>
			<table style="width:100%">
				<form:form name="search" method="GET" action="${path}" >
					<tr>
						<th style="text-align: center; vertical-align: bottom; padding: 5px;">
						<strong><spring:message code="game.platform"/></strong>
						</th>
						<th style="text-align: center; vertical-align: bottom; padding: 5px;">
							<strong><spring:message code="game.genres"/></strong>
						</th>
						<th style="text-align: right; vertical-align: bottom; padding: 5px 45px;">
							<strong><spring:message code="game.score"/></strong>
						</th>
						<th style="text-align: center; vertical-align: bottom; padding: 5px;">
							<strong><spring:message code="game.timeSearch"/></strong>
						</th>
					</tr>
					<tr>
						<td style="text-align: center; vertical-align: middle; padding:5px 5px">
							<div class="form-group">
								<input type="hidden" value="${searchTerm}" name="search"/>
								<input type="hidden" value="1" name="page"/>
								<select class="form-control" name="platforms" size="5" id="platforms" class="search-filters" multiple>
									<c:forEach var="platform" items="${platforms}">
										<option value="${platform.id}">${platform.name}</option>
									</c:forEach>
								</select>
							</div>
						</td>
						<td style="text-align: center; vertical-align: middle; padding:5px 5px">
							<div class="form-group">
								<select class="form-control" name="genres" size="5" id="genres" multiple>
									<c:forEach var="genre" items="${genres}">
										<option value="${genre.id}"><spring:message code="genres.${genre.name}"/></option>
									</c:forEach>
								</select>
							</div>
						</td>
						<td style="text-align: right; vertical-align: top; padding:0px 3px">
							<input type="number" min="0" max="100" id="scoreLeft" name="scoreLeft" value="${scoreLeft}"/><strong> - </strong>
							<input type="number" min="0" max="100" id="scoreRight" name="scoreRight" value="${scoreRight}"/>
						</td>
						<td style="text-align: center; vertical-align: top;">
						<div class="form-group">
							<input type="number" min="0" max="9999" id="hoursLeft" name="hoursLeft" value="${hoursLeft}"/><strong> :</strong>
							<input type="number" min="0" max="59" id="minsLeft" name="minsLeft" value="${minsLeft}"/><strong> :</strong>
							<input type="number" min="0" max="59" id="secsLeft" name="secsLeft" value="${secsLeft}"/><strong> - </strong>
							<input type="number" min="0" max="9999" id="hoursRight" name="hoursRight" value="${hoursRight}"/><strong> :</strong>
							<input type="number" min="0" max="59" id="minsRight" name="minsRight" value="${minsRight}"/><strong> :</strong>
							<input type="number" min="0" max="59" id="secsRight" name="secsRight" value="${secsRight}"/>
						</div>
						</td>
						<td style="text-align: left; vertical-align: top;" >		
							<input type="submit" class="btn btn-primary btn-lg button" value="<spring:message code="game.filter"/>">
						</td>
					</tr>
				</form:form>
			</table>
		</div>
	</div>
	</div>
    <div class="game-search-results">
        <c:set var="listName"><spring:message code="search.results" arguments="${searchTerm}"/></c:set>
        <%@ include file="../common/gameList.jsp"%>
    </div>
    <c:if test="${empty games}">
		<div class="main-game-lists-popular">
			<spring:message code="index.popular" var="popular"/>
			<c:set var="listName" value="${popular}"/>
			<c:set var="games" value="${popularGames}"/>
			<%@ include file="../common/gameList.jsp"%>
		</div>
	</c:if>

<div class="col mb-5">
	<div class="row text-center">
		<c:choose>
			<c:when test="${current != 1}">
				<div class="col">
					<form:form name="searchPrev" method="GET" action="${path}">
					<input type="hidden" value="${searchTerm}" name="search"/>
					<input type="hidden" value="${current - 1}" name="page"/>
					<input type="hidden" value="${hoursLeft}" name="hoursLeft"/>
					<input type="hidden" value="${minsLeft}" name="minsLeft"/>
					<input type="hidden" value="${secsLeft}" name="secsLeft"/>
					<input type="hidden" value="${hoursRight}" name="hoursRight"/>
					<input type="hidden" value="${minsRight}" name="minsRight"/>
					<input type="hidden" value="${secsRight}" name="secsRight"/>
					<input type="hidden" value="${scoreLeft}" name="scoreLeft"/>
					<input type="hidden" value="${scoreRight}" name="scoreRight"/>
					<input type="hidden" value="${currentPlats}" name="platforms"/>
					<input type="hidden" value="${currentGens}" name="genres"/>
					<input class="btn btn-dark" type="submit" value="<spring:message code="search.prev"/>"/>
					</form:form>
				</div>
			</c:when>
			<c:otherwise>
				<div class="col">
						<input class="btn btn-light" type="submit" disabled value="<spring:message code="search.prev"/>"/>
				</div>                 
			</c:otherwise>
		</c:choose>
           
           <div class="col row">
		<c:forEach begin="1" end="${pages}" var="num">
			<div class="col-xs mx-auto">
				<c:choose>
					<c:when test="${current == num}">
						<input class="btn btn-light" type="submit" disabled value="${num}"/>
					</c:when>
					<c:otherwise>
						<form:form name="searchPage" method="GET" action="${path}">
							<input type="hidden" value="${searchTerm}" name="search"/>
							<input type="hidden" value="${num}" name="page"/>
							<input type="hidden" value="${hoursLeft}" name="hoursLeft"/>
							<input type="hidden" value="${minsLeft}" name="minsLeft"/>
							<input type="hidden" value="${secsLeft}" name="secsLeft"/>
							<input type="hidden" value="${hoursRight}" name="hoursRight"/>
							<input type="hidden" value="${minsRight}" name="minsRight"/>
							<input type="hidden" value="${secsRight}" name="secsRight"/>
							<input type="hidden" value="${scoreLeft}" name="scoreLeft"/>
							<input type="hidden" value="${scoreRight}" name="scoreRight"/>
							<input type="hidden" value="${currentPlats}" name="platforms"/>
							<input type="hidden" value="${currentGens}" name="genres"/>
							<input class="btn btn-dark" type="submit" value="${num}"/>
						</form:form>                   
					</c:otherwise>
				</c:choose>
			</div>
		</c:forEach>
		</div>
           
           <c:choose>
			<c:when test="${current < pages}">
				<div class="col">
					<form:form name="searchNext" method="GET" action="${path}">
					<input type="hidden" value="${searchTerm}" name="search"/>
					<input type="hidden" value="${current + 1}" name="page"/>
					<input type="hidden" value="${hoursLeft}" name="hoursLeft"/>
					<input type="hidden" value="${minsLeft}" name="minsLeft"/>
					<input type="hidden" value="${secsLeft}" name="secsLeft"/>
					<input type="hidden" value="${hoursRight}" name="hoursRight"/>
					<input type="hidden" value="${minsRight}" name="minsRight"/>
					<input type="hidden" value="${secsRight}" name="secsRight"/>
					<input type="hidden" value="${scoreLeft}" name="scoreLeft"/>
					<input type="hidden" value="${scoreRight}" name="scoreRight"/>
					<input type="hidden" value="${currentPlats}" name="platforms"/>
					<input type="hidden" value="${currentGens}" name="genres"/>
					<input class="btn btn-dark" type="submit" value="<spring:message code="search.next"/>"/>
					</form:form>
				</div>
			</c:when>
			<c:otherwise>
				<div class="col">
						<input class="btn btn-light" type="submit" disabled value="<spring:message code="search.next"/>"/>
				</div>         
			</c:otherwise>
		</c:choose>
	</div>
</div>
</div>
</body>
</html>

<script type="text/javascript" src="<c:url value="/js/filters/search-filters.js"/>"></script>

