<%--
    Include this page:
        <%@ include file="gameListItem.jsp"%>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="card m-5 bg-very-light">
    <div class="card-header bg-very-dark text-white">
        <h2>${listName}</h2>
    </div>
    <div class="card-body d-flex flex-wrap justify-content-center">
        <c:if test="${empty games}">
            <p><spring:message code="gameList.empty"/></p>
        </c:if>
        <c:forEach var="game" items="${games}">
            <%@ include file="gameListItem.jsp"%>
        </c:forEach>
    </div>
</div>
