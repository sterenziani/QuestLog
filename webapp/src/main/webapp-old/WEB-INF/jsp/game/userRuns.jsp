<div class="card m-5 bg-very-light right-wave left-wave">
    <div class="card-header bg-very-dark text-white d-flex">
    	<div>
            <h2 class="share-tech-mono"><spring:message code="game.yourRuns"/></h2>
        </div>
	</div>
	<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
		<div class="container">
			<div class="row">
				<div class="col text-center bg-primary text-white"><strong><spring:message code="game.platform"/></strong></div>
				<div class="col text-center bg-primary text-white"><strong><spring:message code="game.playstyle"/></strong></div>
				<div class="col text-center bg-primary text-white"><strong><spring:message code="game.yourTime"/></strong></div>
			</div>
			<c:forEach var="element" items="${user_runs}">
				<div class="row">
					<div class="col text-center"><c:out value="${element.platform.shortName}"/></div>
					<div class="col text-center"><spring:message code="playstyle.${element.playstyle.name}"/></div>
					<div class="col text-center"><c:out value="${element}"/></div>
				</div>
			</c:forEach>
		</div>
	</div>
</div>