<%--
    Include this page:
        <%@ include file="gameDetailsInfoItem.jsp"%>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="game-details-info-item">
    <dt><strong><c:out value="${title}"/></strong></dt>
    <span class="game-details-info-item-spacing"></span>
    <dd>
        <ul>
            <c:forEach var="item" items="${items}">
                <li><c:out value="${item}"/></li>
            </c:forEach>
        </ul>
    </dd>
</div>