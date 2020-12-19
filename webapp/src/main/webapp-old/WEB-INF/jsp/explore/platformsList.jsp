<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<div class="card m-5 bg-very-light right-wave left-wave">
	<div class="card-header bg-very-dark text-white d-flex">
		<div>
			<h2 class="share-tech-mono"><spring:message code="explore.platforms"/></h2>
		</div>
		<div class="ml-auto">
			<c:if test="${!empty seeAllPlatformsUrl}">
	            <div class="ml-auto">
	                <a class="btn btn-link text-white" href="<c:url value="${seeAllPlatformsUrl}"/>"><spring:message code="explore.seeAll"/></a>
	            </div>
	        </c:if>
		</div>
	</div>
	<div class="card-body d-flex flex-wrap justify-content-center">
		<c:forEach items="${platforms}" var="platform">
			<a href="<c:url value="platforms/${platform.id}"/>">
				<div class="card m-3 d-flex bg-transparent" style="width: 10rem;">
					<a class="d-flex flex-column flex-grow-1 text-white text-center align-center" href="<c:url value="/platforms/${platform.id}"/>">
						<div class="m-auto"><img class="platform-icon m-auto card-img-top" src="<c:url value="/images/${platform.logo}"/>" alt="<c:out value="${platform.shortName}"/>"/></div>
						<div class="card-body bg-primary flex-grow-1">
							<h5><c:out value="${platform.name}"/></h5>
						</div>
					</a>
				</div>
			</a>
		</c:forEach>
	</div>
</div>