<div class="card m-5 bg-very-light right-wave left-wave">
    <div class="card-header bg-very-dark text-white d-flex">
    	<div>
            <h2 class="share-tech-mono"><spring:message code="game.fastestRuns"/></h2>
        </div>
	</div>
	<c:if test="${!empty topRuns}">
		<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
			<div class="container">
				<c:forEach var="element" items="${topRuns}">
					<div class="row">
						<div class="col text-right"><a href="<c:url value="/users/${element.user.id}"/>"><c:out value="${element.user}"/></a></div>
						<div class="col text-center"><c:out value="${element.platform.shortName}"/></div>
						<div class="col text-center"><c:out value="${element}"/></div>
					</div>
				</c:forEach>
			</div>
		</div>
	</c:if>
	<c:if test="${empty topRuns}">
		<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
			<div class="container text-center">
				<spring:message code="game.noRuns"/>
			</div>
		</div>
	</c:if>
</div>