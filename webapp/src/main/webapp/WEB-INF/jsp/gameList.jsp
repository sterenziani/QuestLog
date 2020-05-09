<%--
    Include this page:
        <%@ include file="gameListItem.jsp"%>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="card m-5 bg-very-light right-wave left-wave">
    <div class="card-header bg-very-dark text-white d-flex">
        <div>
            <h2 class="share-tech-mono">${listName}</h2>
        </div>
        <c:if test="${!empty seeAllUrl}">
            <div class="ml-auto">
                <a class="btn btn-primary" href="<c:url value="${seeAllUrl}"/>"><spring:message code="explore.seeAll"/></a>
            </div>
        </c:if>
    </div>
    <div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
        <c:if test="${empty games}">
            <p><spring:message code="gameList.empty"/></p>
        </c:if>
        <c:forEach var="game" items="${games}">
            <%@ include file="gameListItem.jsp"%>
        </c:forEach>
    </div>
</div>
