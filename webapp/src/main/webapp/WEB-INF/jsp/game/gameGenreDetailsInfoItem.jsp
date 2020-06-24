<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<div class="game-details-info-item">
    <div class="bg-dark text-center"><strong><c:out value="${title}"/></strong></div>
    <span class="game-details-info-item-spacing"></span>
    <div class="bg-primary text-center">
        <ul>
            <c:forEach var="item" items="${items}">
                <spring:message code="genres.${item}" var="genreItem"/>
                <c:out value="${genreItem}"/>
                <br>
            </c:forEach>
        </ul>
    </div>
</div>