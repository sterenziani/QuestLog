<%--
    Include this page:
        <%@ include file="gameDetailsInfoItem.jsp"%>

    Including jsp should have:
        * variable title
        * variable items
        * inside the header:
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetailsInfoItem.css"/>">

--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="game-details-info-item">
    <dt><strong>${title}</strong></dt>
    <dd>
        <ul>
            <c:forEach var="item" items="${items}">
                <li>${item}</li>
            </c:forEach>
        </ul>
    </dd>
</div>