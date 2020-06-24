<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
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
	<div id="search-content" class="d-flex flex-row">
		<div class="search-parameters col-3 px-1">
			<div class="container text-center align-middle">
				<div class="my-5 p-3 ml-5 bg-light border-bottom border-primary rounded-lg">
					<h2 class="share-tech-mono mb-3"><spring:message code="search.filters"/></h2>
					<div>
						<form:form name="search" method="GET" action="${path}" >
							<div class="form-group">
								<h5><strong><spring:message code="game.platform"/></strong></h5>
								<div class="form-group">
									<input type="hidden" value="${searchTerm}" name="search"/>
									<input type="hidden" value="1" name="page"/>
									<select class="form-control advanced-select" name="platforms" size="5" id="platforms" class="search-filters" multiple>
										<c:forEach var="platform" items="${platforms}">
											<option value="${platform.id}"><c:out value="${platform.name}"/></option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group">
								<h5><strong><spring:message code="game.genres"/></strong></h5>
								<div class="form-group">
									<select class="form-control advanced-select" name="genres" size="5" id="genres" multiple>
										<c:forEach var="genre" items="${genres}">
											<option value="${genre.id}"><spring:message code="genres.${genre.name}"/></option>
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="my-auto form-group">
								<h5><label for="scoreLeft"><strong><spring:message code="game.score"/></strong></label></h5>
								<div class="d-flex form-group align-items-center">
									<input type="number" min="0" max="100" id="scoreLeft" name="scoreLeft" value="${scoreLeft}" class="form-control"/><span class="mx-2">&mdash;</span>
									<input type="number" min="0" max="100" id="scoreRight" name="scoreRight" value="${scoreRight}" class="form-control"/>
								</div>
							</div>
							<div class="form-group">
								<h5><label for="hoursLeft"><strong><spring:message code="game.timeSearch"/></strong></label></h5>
								<div class="form-group">
										<div class="mr-2">
											<label for="hoursLeft" class="mb-0"><strong><spring:message code="game.time.from"/></strong></label>
										</div>
									<div class="d-flex form-group align-items-center">
										<div class="d-flex form-group mb-0 text-center col align-items-center">
											<input type="number" min="0" max="9999" id="hoursLeft" name="hoursLeft" value="${hoursLeft}" class="form-control max-9999 w-100"/><span class="ml-1"><spring:message code="game.time.hours"/></span>
											<input type="number" min="0" max="59" id="minsLeft" name="minsLeft" value="${minsLeft}" class="form-control max-59 w-75"/><span class="ml-1"><spring:message code="game.time.minutes"/></span>
										</div>
									</div>
									<div class="mr-2">
										<label for="hoursRight" class="mb-0"><strong><spring:message code="game.time.to"/></strong></label>
									</div>
									<div class="d-flex form-group align-items-center">
										<div class="d-flex form-group mb-0 text-center col align-items-center">
											<input type="number" min="0" max="9999" id="hoursRight" name="hoursRight" value="${hoursRight}" class="form-control max-9999 w-100"/><span class="ml-1"><spring:message code="game.time.hours"/></span>
											<input type="number" min="0" max="59" id="minsRight" name="minsRight" value="${minsRight}" class="form-control max-59 w-75"/><span class="ml-1"><spring:message code="game.time.minutes"/></span>
										</div>
									</div>
								</div>
							</div>
							<input type="submit" class="btn btn-primary btn-lg button btn-block" value="<spring:message code="game.filter"/>">
						</form:form>
					</div>
				</div>
			</div>
		</div>
		<div id="game-search-results-and-control" class="col-9 px-1">
			<div class="game-search-results">
				<c:choose>
					<c:when test="${empty searchTerm}">
						<c:set var="listName"><spring:message code="search.results" arguments="*"/></c:set>
					</c:when>
					<c:otherwise>
						<c:set var="listName"><spring:message code="search.results" arguments="${searchTerm}"/></c:set>
					</c:otherwise>
				</c:choose>
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
									<input class="btn btn-light mb-2" type="submit" disabled value="${num}"/>
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
	</div>
</div>
</body>
</html>

<script type="text/javascript" src="<c:url value="/js/filters/search-filters.js"/>"></script>
<script>
	var max_9999 = document.getElementsByClassName('max-9999');
	var i;
	for(i = 0; i < max_9999.length; i++){
		max_9999[i].oninput = function () {
			var max = parseInt(this.max);

			if (parseInt(this.value) > max) {
				this.value = max;
			}
		}
	}
	var max_59 = document.getElementsByClassName('max-59');
	for(i = 0; i < max_59.length; i++){
		max_59[i].oninput = function () {
			var max = parseInt(this.max);

			if (parseInt(this.value) > max) {
				this.value = max;
			}
		}
	}
</script>

