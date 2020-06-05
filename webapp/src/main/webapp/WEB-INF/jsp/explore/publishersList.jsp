<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="card m-5 bg-very-light right-wave left-wave">
	<div class="card-header bg-very-dark text-white d-flex">
		<div>
			<h2 class="share-tech-mono"><spring:message code="explore.publishers"/></h2>
		</div>
		<c:if test="${!empty seeAllPubsUrl}">
            <div class="ml-auto">
                <a class="btn btn-link text-white" href="<c:url value="${seeAllPubsUrl}"/>"><spring:message code="explore.seeAll"/></a>
            </div>
        </c:if>
	</div>
	<div class="card-body d-flex flex-wrap justify-content-center">
		<c:forEach items="${publishers}" var="publisher">
			<a href="<c:url value="/publishers/${publisher.id}"/>">
				<div class="card m-3 d-flex bg-transparent" style="width: 10rem;">
					<a class="d-flex flex-column flex-grow-1 text-white text-center align-center" href="<c:url value="/publishers/${publisher.id}"/>">
						<div class="card-body bg-primary flex-grow-1">
							<h5><c:out value="${publisher.name}"/></h5>
						</div>
					</a>
				</div>
			 </a>
		</c:forEach>
	</div>	
</div>