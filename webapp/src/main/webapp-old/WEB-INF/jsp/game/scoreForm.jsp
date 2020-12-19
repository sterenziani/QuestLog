<div class="container">	
<div class="row">
	<div class="col-sm-3">
		<div class="row"><strong class="score-title"><spring:message code="game.averageUserScore"/></strong></div>
		<div class="row">
			<c:choose>
				<c:when test="${empty averageScore}">
					<spring:message code="game.notAvailable" var="notAvailable"/>
					<p class="score-display badge badge-dark score-display-avg"><c:out value="${notAvailable}"/></p>
				</c:when>
				<c:otherwise>
					<p class="score-display badge badge-dark"><c:out value="${averageScore}"/></p>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
	<div class="col-sm-9">
	<form:form name="scores" method="POST" action="scores/${game.id}">
		<div class="row">
			<div class="col-sm-7 my-auto">
					<input id="removeFromBacklogInput" type="hidden" value="false" name="removeFromBacklog"/>
					<div class="row m-auto">
						<spring:message code="game.yourScore" var="yourScore"/>
						<strong class="score-title"><c:out value="${yourScore}"/></strong>
					</div>
					<spring:message code="game.yourScore" var="score"/>
					<c:choose>
						<c:when test="${empty user_score}">
							<div class="score-slider">
								<input class="slider mt-3 mb-3" id="range-slider" type="range" name="score" min="0" max="100" oninput="scoreText.innerHTML = document.getElementById('range-slider').value" value="50">
							</div>
						</c:when>
						<c:otherwise>
							<div class="score-slider">
								<input class="slider mt-3 mb-3" id="range-slider" type="range" name="score" min="0" max="100" oninput="scoreText.innerHTML = document.getElementById('range-slider').value" value="<c:out value="${user_score.score}"/>"/>
							</div>
						</c:otherwise>
					</c:choose>
					<input type="hidden" value="<c:out value="${game.id}"/>" name="game"/>
			</div>
			<div class="col">
				<c:choose>
					<c:when test="${empty user_score}">
						<div class="text-center px-5"><div class="score-number">
							<p class="display-4 score-display badge badge-success" id="scoreText">-</p>
						</div></div>
					</c:when>
					<c:otherwise>
						<div class="text-center px-5"><div class="score-number">
							<p class="score-display badge badge-success" id="scoreText"><c:out value="${user_score.score}"/></p>
						</div></div>
					</c:otherwise>
				</c:choose>
				<div class="score-submit px-5">
		            <c:choose>
		                <c:when test="${game.inBacklog && loggedUser != null}">
							<spring:message code="game.rate" var="rate"/>
							<input type="button" class="btn btn-primary btn-block score-submit-button button" value="<c:out value="${rate}"/>" data-toggle="modal" data-target="#removeGameFromBacklogModal-<c:out value="${game.id}"/>"/>
				            
				            <div class="modal fade" id="removeGameFromBacklogModal-<c:out value="${game.id}"/>" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
				                <div class="modal-dialog modal-dialog-centered" role="document">
				                    <div class="modal-content">
				                        <div class="modal-header">
											<spring:message code="game.removeFromBacklogAsk" arguments="${game.title}" var="removeFromBacklogAsk"/>
				                            <h5 class="modal-title" id="removeGameFromBacklogLabel-<c:out value="${game.id}"/>"><c:out value="${removeFromBacklogAsk}"/></h5>
				                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
				                                <span aria-hidden="true">&times;</span>
				                            </button>
				                        </div>
				                        <div class="modal-body">
				                            <spring:message code="game.removeFromBacklogExplain" var="removeFromBacklogExplain"/>
											<c:out value="${removeFromBacklogExplain}"/>
				                        </div>
				                        <div class="modal-footer">
											<spring:message code="game.postButKeepInBacklog" var="postButKeepInBacklog"/>
				                            <button type="button" id="postButKeepInBacklogButton" class="btn btn-light" data-dismiss="modal"><c:out value="${postButKeepInBacklog}"/></button>
											<spring:message code="game.postAndRemoveFromBacklog" var="postAndRemoveFromBacklog"/>
				                            <a id="postAndRemoveFromBacklogButton" class="btn btn-primary text-white"><c:out value="${postAndRemoveFromBacklog}"/></a>
				                        </div>
				                    </div>
				                </div>
				            </div>
		                </c:when>
		                <c:otherwise>
							<spring:message code="game.rate" var="rate"/>
							<input type="submit" class="btn btn-primary btn-block score-submit-button button" value="<c:out value="${rate}"/>"/>
		                </c:otherwise>
		            </c:choose>
				</div>
			</div>
		</div>
	</form:form>
	</div>
</div>
</div>