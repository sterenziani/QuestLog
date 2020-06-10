<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="panel panel-default bg-dark-white rounded-lg my-3">
  <div class="panel-heading py-2 px-3 bg-light">
	<div class="row">
		<div class="col text-left font-weight-bold">
			<a href="<c:url value="/users/${element.user.id}"/>"><c:out value="${element.user}"/></a>
		</div>
		<div class="col text-right">
			<c:out value="${element.postDate}"/>
		</div>
	</div>
  </div>
  <div class="panel-body p-3">
	<div class="row mb-3">
		<div class="col text-center font-weight-bold">
			<a href="<c:url value="/games/${element.game.id}"/>"><c:out value="${element.game}"/></a> (<c:out value="${element.platform}"/>)
		</div>
	</div>
	<div class="row">
		<div class="col-10 text-left">
			<c:out value="${element.body}"/>
		</div>
		<div class="col-2 container">
			<div class="row">
				<p class="score-display badge badge-dark"><c:out value="${element.score}"/></p>
			</div>
		</div>
	</div>
	<div class="row mt-3 justify-content-center">
		<c:if test="${!empty loggedUser && loggedUser.adminStatus == true || loggedUser.id == element.user.id}">
			<c:url value="/reviews/${element.id}/delete" var="post_url"/>
		    <spring:message code="game.delete" var="deleteReview"/>
            <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#deleteReviewModal-${element.id}">
                <c:out value="${deleteReview}"/>
            </button>
            <div class="modal fade" id="deleteReviewModal-${element.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="deleteReviewModalLabel-${element.id}"><spring:message code="review.delete"/></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <spring:message code="review.delete.message"/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-light" data-dismiss="modal"><spring:message code="review.delete.dismiss"/></button>
							<c:url value="/reviews/${element.id}/delete" var="post_url"/>
						    <form method="post" action="${post_url}">
						    	<spring:message code="game.delete" var="deleteReview"/>
						    	<input class="btn btn-danger" type="submit" value="${deleteReview}"/>
						    </form>
                        </div>
                    </div>
                </div>
            </div>

		</c:if>
	</div>
  </div>
</div>