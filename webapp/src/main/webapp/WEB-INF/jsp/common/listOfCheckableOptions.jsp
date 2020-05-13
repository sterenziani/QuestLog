
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="form-group checkboxes px-3 py-2 bg-very-light">
    <c:forEach var="item" items="${items}">
        <div class="form-check">
            <form:checkbox path="${path}" id="${path}-${item.id}" value="${item.id}" cssClass="form-check-input"/>
            <label class="form-check-label" for="${path}-${item.id}"><c:out value="${item.name}" /></label>
        </div>
    </c:forEach>
</div>