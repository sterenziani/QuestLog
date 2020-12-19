<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<div class="card m-5 bg-light-grey right-wave left-wave">
    <div class="card-header bg-very-dark text-white d-flex">
    	<div>
            <h2 class="share-tech-mono"><c:out value="${reviewsListName}"/></h2>
        </div>
	    <c:if test="${!empty seeAllReviewsUrl}">
	        <div class="ml-auto">
	            <a class="btn btn-link text-white" href="<c:url value="${seeAllReviewsUrl}"/>"><spring:message code="explore.seeAll"/></a>
	        </div>
	    </c:if>
	</div>
	<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
		<c:if test="${empty reviewsInPage}">
           	<div class="justify-content-center text-center mx-auto"><c:out value="${emptyListMessage}"/></div>
       	</c:if>
       	<div class="container">
		<c:forEach var="element" items="${reviewsInPage}">
				<%@ include file="../common/reviewItem.jsp"%>
		</c:forEach>
		</div>
    </div>
</div>