<%--
    Include this page:
        <%@ include file="gameListItem.jsp"%>
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="card m-3 d-flex bg-transparent" style="width: 16rem;">
    <c:if test="${loggedUser != null && loggedUser.adminStatus == true}">
        <div class="d-flex">
            <spring:message code="game.edit" var="edit"/>
            <spring:message code="game.delete" var="delete"/>
            <a href="<c:url value="/admin/game/${game.id}/edit"/>" class="btn btn-block btn-outline-warning not-rounded-bottom btn-lg"><c:out value="${edit}"/></a>
            <button type="button" class="btn btn-block btn-outline-danger not-rounded-bottom btn-lg mt-0" data-toggle="modal" data-target="#deleteGameModal-${game.id}">
                <c:out value="${delete}"/>
            </button>
            <div class="modal fade" id="deleteGameModal-${game.id}" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="deleteGameModalLabel-${game.id}"><spring:message code="game.delete"/><c:out value="${game.title}"/></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        <div class="modal-body">
                            <spring:message code="game.delete.message"/>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-light" data-dismiss="modal"><spring:message code="game.delete.dismiss"/></button>
							<c:url value="/admin/game/${game.id}/delete" var="post_url"/>
						    <form method="post" action="${post_url}">
						    	<spring:message code="game.delete" var="deleteReview"/>
						    	<input class="btn btn-danger" type="submit" value="<spring:message code="game.delete.confirm"/>"/>
						    </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <form method="post" onsubmit="var buttons = document.getElementsByClassName('button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].disabled=true;}">
        <input type="hidden" name="gameId" value="<c:out value="${game.id}"/>">
        <spring:message code="game.addToBacklog" var="addToBacklog"/>
        <spring:message code="game.addingToBacklog" var="addingToBacklog"/>
        <spring:message code="game.removeFromBacklog" var="removeFromBacklog"/>
        <spring:message code="game.removingFromBacklog" var="removingFromBacklog"/>
        <c:choose>
            <c:when test="${game.inBacklog}">
                <input class="btn btn-block btn-outline-danger btn-lg not-rounded-bottom button-${game.id} remove-button-${game.id}" type="submit" onclick="var buttons = document.getElementsByClassName('remove-button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].value = '${removingFromBacklog}';}" value="${removeFromBacklog}"/>
            </c:when>
            <c:otherwise>
                <input class="btn btn-block btn-outline-success btn-lg not-rounded-bottom button-${game.id} add-button-${game.id}" type="submit" onclick="var buttons = document.getElementsByClassName('add-button-${game.id}'); for(var i=0; i < buttons.length; i++){buttons[i].value = '${addingToBacklog}';}" value="${addToBacklog}"/>
            </c:otherwise>
        </c:choose>
    </form>

    <a class="d-flex flex-column flex-grow-1 text-white" href="<c:url value="/games/${game.id}"/>">
        <img class="card-img-top cover" src="<c:url value="/images/${game.cover}"/>" alt="<c:out value="${game.title}"/>"/>
        <div class="card-body bg-primary flex-grow-1">
            <h5><c:out value="${game.title}"/></h5>
        </div>
    </a>
</div>