<div class="card m-5 bg-very-light right-wave left-wave">
    <div class="card-header bg-very-dark text-white d-flex">
    	<div>
            <h2 class="share-tech-mono"><spring:message code="game.averageTime"/></h2>
        </div>
	</div>
	<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
		<div class="container">
			<div class="row">
				<div class="col text-right bg-primary text-white"><strong><spring:message code="game.playstyle"/></strong></div>
				<div class="col bg-primary text-white"><strong><spring:message code="game.averageTime"/></strong></div>
			</div>
			<c:forEach var="element" items="${playAverage}">
				<div class="row">
					<div class="col text-right"><spring:message code="playstyle.${element.key}"/></div>
					<div class="col">
						<c:if test="${element.value != '0 : 00 : 00'}">
							<c:out value="${element.value}"/>
						</c:if>
						<c:if test="${element.value == '0 : 00 : 00'}">
							<spring:message code="game.notAvailable"/>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</div>