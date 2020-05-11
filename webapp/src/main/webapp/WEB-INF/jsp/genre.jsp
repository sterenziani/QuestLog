<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
    <c:url value="/genres" var="path"/>
    	<div class="logo"><img class="page-header-image" src="${genre.logo}"></img></div>
        <div>
        	<spring:message code="genres.${genre.name}" var="genreName"/>
	        <spring:message code="genre.gamesOfGenre" arguments="${genreName}" var="gamesOfGenre"/>
	        <c:set var="games" value="${games}"/>
	        <c:set var="listName" value="${gamesOfGenre}"/>
	        <%@ include file="gameList.jsp"%>
    	</div>
    	<div>
   		<table class="table-pagination">
        <tr>
            <c:if test="${current != 1}">
            <form action="${path}">
            <input type="hidden" value="${genre.id}" name="genre"/>
			<input type="hidden" value="${current - 1}" name="page"/>
        	<td><input class="pagination" type="submit" value="<spring:message code="search.prev"/>"/></td>
        	</form>
   			 </c:if>
            <c:forEach begin="1" end="${pages}" var="num">
                <c:choose>
                    <c:when test="${current == num}">
                        <td class="current-pagination">${num}</td>
                    </c:when>
                    <c:otherwise>
           			<form action="${path}">
           			<input type="hidden" value="${genre.id}" name="genre"/>
					<input type="hidden" value="${num}" name="page"/>
					<td><input class="pagination" type="submit" value="${num}"/></td>
					</form>                   
					</c:otherwise>
                </c:choose>
            </c:forEach>
            <c:if test="${current < pages}">
            <form action="${path}">
            <input type="hidden" value="${genre.id}" name="genre"/>
			<input type="hidden" value="${current + 1}" name="page"/>			
        	<td><input class="pagination" type="submit" value="<spring:message code="search.next"/>"/></td>
			</form>
    		</c:if>
        </tr>
   		</table>
    	</div>
    </div>
</body>
</html>