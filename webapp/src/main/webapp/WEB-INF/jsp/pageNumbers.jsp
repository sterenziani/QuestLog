<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<table>
    <tr>
        <c:if test="${current != 1}">
			<form:form name="searchPrev" method="GET" action="${listPath}">
				<input type="hidden" value="${current - 1}" name="page"/>
				<td><input type="submit" value="<spring:message code="search.prev"/>"/></td>
			</form:form>
		</c:if>
		<c:forEach begin="1" end="${pages}" var="num">
			<c:choose>
				<c:when test="${current == num}">
					<td>${num}</td>
				</c:when>
				<c:otherwise>
					<form:form name="searchPage" method="GET" action="${listPath}">
						<input type="hidden" value="${num}" name="page"/>
						<td><input type="submit" value="${num}"/>"/></td>
					</form:form>                   
				</c:otherwise>
			</c:choose>
		</c:forEach>
		<c:if test="${current < pages}">
			<form:form name="searchNext" method="GET" action="${listPath}">
				<input type="hidden" value="${current + 1}" name="page"/>
				<td><input type="submit" value="<spring:message code="search.next"/>"/></td>
			</form:form>
 		</c:if>
    </tr>
</table>