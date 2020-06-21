<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="card m-5 bg-very-light right-wave left-wave">
	<div class="card-header bg-very-dark text-white d-flex">
		<div>
			<h2 class="share-tech-mono"><spring:message code="explore.genres"/></h2>
		</div>
		<div class="ml-auto">
			<c:if test="${!empty seeAllGenresUrl}">
	            <div class="ml-auto">
	                <a class="btn btn-link text-white" href="<c:url value="${seeAllGenresUrl}"/>"><spring:message code="explore.seeAll"/></a>
	            </div>
	        </c:if>
		</div>
	</div>
	<div class="card-body d-flex flex-wrap justify-content-center">
		<c:forEach items="${genres}" var="genre">
			<a href="<c:url value="genres/${genre.id}"/>">
				<div class="card m-3 d-flex bg-transparent" style="width: 10rem;">
					<a class="d-flex flex-column flex-grow-1 text-white text-center align-center" href="<c:url value="/genres/${genre.id}"/>">
						<img class="m-auto card-img-top genre-icon" src="<c:url value="/images/${genre.logo}"/>" alt="<c:out value="${genre.name}"/>"/>
						<div class="card-body bg-primary flex-grow-1">
							<h5><spring:message code="genres.${genre.name}"/></h5>
						</div>
					</a>
				</div>
			</a>
		</c:forEach>
	</div>
</div>