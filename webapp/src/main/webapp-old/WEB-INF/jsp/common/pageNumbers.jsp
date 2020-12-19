<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<div class="col mb-5">
	<div class="row text-center">
			<c:choose>
				<c:when test="${current != 1}">
					<div class="col">
						<form:form name="searchPrev" method="GET" action="${listPath}">
							<input type="hidden" value="${current - 1}" name="page"/>
							<input class="btn btn-dark" type="submit" value="<spring:message code="search.prev"/>"/>
						</form:form>
					</div>
				</c:when>
				<c:otherwise>
					<div class="col">
							<input class="btn btn-light" type="submit" disabled value="<spring:message code="search.prev"/>"/>
					</div>                 
				</c:otherwise>
			</c:choose>
			
			<div class="col row">
			<c:forEach begin="1" end="${pages}" var="num">
				<div class="col-xs mx-auto">
					<c:choose>
						<c:when test="${current == num}">
							<input class="btn btn-light" type="submit" disabled value="${num}"/>
						</c:when>
						<c:otherwise>
							<form:form name="searchPage" method="GET" action="${listPath}">
								<input type="hidden" value="${num}" name="page"/>
								<input class="btn btn-dark" type="submit" value="${num}"/>
							</form:form>                   
						</c:otherwise>
					</c:choose>
				</div>
			</c:forEach>
			</div>
			
			<c:choose>
				<c:when test="${current < pages}">
					<div class="col">
						<form:form name="searchNext" method="GET" action="${listPath}">
							<input type="hidden" value="${current + 1}" name="page"/>
							<input class="btn btn-dark" type="submit" value="<spring:message code="search.next"/>"/>
						</form:form>
					</div>
				</c:when>
				<c:otherwise>
					<div class="col">
							<input class="btn btn-light" type="submit" disabled value="<spring:message code="search.next"/>"/>
					</div>         
				</c:otherwise>
			</c:choose>
	</div>
</div>