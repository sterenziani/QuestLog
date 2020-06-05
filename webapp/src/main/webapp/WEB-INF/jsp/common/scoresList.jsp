<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="card m-5 bg-very-light right-wave left-wave">
    <div class="card-header bg-very-dark text-white d-flex">
    	<div>
            <h2 class="share-tech-mono"><spring:message code="user.scores" arguments="${user.username}"/></h2>
        </div>
	    <c:if test="${!empty seeAllScoresUrl}">
	        <div class="ml-auto">
	            <a class="btn btn-link text-white" href="<c:url value="${seeAllScoresUrl}"/>"><spring:message code="explore.seeAll"/></a>
	        </div>
	    </c:if>
	</div>
	<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
		<c:if test="${empty scoresInPage}">
           	<p><spring:message code="user.noScores"/></p>
       	</c:if>
       	<div class="container">
		<c:forEach var="element" items="${scoresInPage}">
			<div class="row">
				<div class="col text-right"><a href="<c:url value="/games/${element.game.id}"/>"><c:out value="${element.game}"/></a></div>
				<div class="col"><p class=""><c:out value="${element.score}"/></p></div>
			</div>
		</c:forEach>
		</div>
    </div>
</div>