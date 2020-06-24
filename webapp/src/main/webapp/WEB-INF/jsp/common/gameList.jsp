<%--
    Include this page:
        <%@ include file="gameListItem.jsp"%>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<div class="card m-5 bg-very-light right-wave left-wave">
    <div class="card-header bg-very-dark text-white px-3">
    	<div class="row px-3">
	        <div>
	            <h2 class="share-tech-mono"><c:out value="${listName}"/></h2>
	        </div>
			<c:choose>
				<c:when test="${!empty seeAllUrl}">
		            <div class="ml-auto">
		                <a class="btn btn-link text-white" href="<c:url value="${seeAllUrl}"/>"><spring:message code="explore.seeAll"/></a>
		            </div>
				</c:when>
				<c:when test="${!empty listIcon}">
					<div class="ml-auto">
		                <div class="m-auto"><img class="list-icon page-header-image" src="<c:url value="/images/${listIcon}"/>"/></div>
		            </div>
				</c:when>
			</c:choose>
		</div>
		<div class="row px-3">
			<c:if test="${!empty amountResults}">
				<div class="row ml-1 mr-auto my-auto">
					<span class="text-white"><strong><c:out value="${amountResults}"/></strong> <spring:message code="search.resultsLabel"/></span>
				</div>
			</c:if>
		</div>
    </div>
    <div class="card-body d-flex flex-wrap justify-content-center">
        <c:if test="${empty games}">
            <p><spring:message code="gameList.empty"/></p>
        </c:if>
        <c:forEach var="game" items="${games}">
            <%@ include file="gameListItem.jsp"%>
        </c:forEach>
    </div>
</div>
