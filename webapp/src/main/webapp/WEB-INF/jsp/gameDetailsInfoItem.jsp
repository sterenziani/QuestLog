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