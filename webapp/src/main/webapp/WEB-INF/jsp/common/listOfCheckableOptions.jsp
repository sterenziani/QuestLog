
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="checkbox-array-container">
    <c:forEach var="item" items="${items}">
        <div class="checkbox-container light-primary-color">
            <form:checkbox path="${path}" id="${path}-${item.id}" value="${item.id}"/>
            <label for="${path}-${item.id}"><c:out value="${item.name}"/></label>
        </div>
    </c:forEach>
</div>