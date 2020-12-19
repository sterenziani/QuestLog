<div class="card m-5 bg-very-light right-wave left-wave">
    <div class="card-header bg-very-dark text-white d-flex">
    	<div>
			<spring:message code="game.averageTime" var="avgTime"/>
            <h2 class="share-tech-mono"><c:out value="${avgTime}"/></h2>
        </div>
	</div>
	<div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
		<div class="container">
			<div class="row">
				<spring:message code="game.playstyle" var="playstyle"/>
				<div class="col text-right bg-primary text-white"><strong><c:out value="${playstyle}"/></strong></div>
				<spring:message code="game.averageTime" var="avgTime"/>
				<div class="col bg-primary text-white"><strong><c:out value="${avgTime}"/></strong></div>
			</div>
			<c:forEach var="element" items="${playAverage}">
				<div class="row">
					<spring:message code="playstyle.${element.key}" var="playstyleKey"/>
					<div class="col text-right"><c:out value="${playstyleKey}"/></div>
					<div class="col">
						<c:if test="${element.value != '0 : 00 : 00'}">
							<c:out value="${element.value}"/>
						</c:if>
						<c:if test="${element.value == '0 : 00 : 00'}">
							<spring:message code="game.notAvailable" var="notAv"/>
							<c:out value="${notAv}"/>
						</c:if>
					</div>
				</div>
			</c:forEach>
		</div>
	</div>
</div>