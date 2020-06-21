<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<div class="card m-5 bg-very-light right-wave left-wave">
    <div class="card-header bg-very-dark text-white d-flex">
    	<div>
            <h2 class="share-tech-mono"><spring:message code="user.runs" arguments="${user.username}"/></h2>
        </div>
	    <c:if test="${!empty seeAllRunsUrl}">
	        <div class="ml-auto">
	            <a class="btn btn-link text-white" href="<c:url value="${seeAllRunsUrl}"/>"><spring:message code="explore.seeAll"/></a>
	        </div>
	    </c:if>
	</div>
	<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
		<c:if test="${empty runsInPage}">
           	<div class="justify-content-center text-center mx-auto"><p><spring:message code="user.noScores"/></p></div>
       	</c:if>
       	<div class="container">
		<c:forEach var="element" items="${runsInPage}">
			<div class="row">
				<div class="col-sm-4 text-right"><a href="<c:url value="/games/${element.game.id}"/>"><c:out value="${element.game}"/></a></div>
				<div class="col-sm-2 text-center"><c:out value="${element.platform.shortName}"/></div>
				<div class="col-sm-3 text-center"><spring:message code="playstyle.${element.playstyle.name}"/></div>
				<div class="col-sm-2 text-center"><c:out value="${element}"/></div>
			</div>
		</c:forEach>
		</div>
    </div>
</div>