<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
	<c:choose>
		<c:when test="${empty searchTerm}">
			<title>QuestLog - <spring:message code="search.results" arguments="*"/></title>
		</c:when>
		<c:otherwise>
			<title>QuestLog - <spring:message code="search.results" arguments="${searchTerm}"/></title>
		</c:otherwise>
    </c:choose>
    <title>QuestLog - <spring:message code="search.results" arguments="${searchTerm}"/></title>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
    <div class="card m-5 bg-very-light right-wave left-wave">
		<div class="card-header bg-very-dark text-white d-flex">
			<div>
				<c:choose>
					<c:when test="${empty searchTerm}">
						<h2 class="share-tech-mono"><spring:message code="search.results" arguments="*"/></h2>
					</c:when>
					<c:otherwise>
						<h2 class="share-tech-mono"><spring:message code="search.results" arguments="${searchTerm}"/></h2>
					</c:otherwise>
         		</c:choose>
			</div>        
        </div>
        <div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
        <div>
        	<c:if test="${empty users}">
            <p><spring:message code="userList.empty"/></p>
       		 </c:if>
        	<table class="table-for-users" style="width:100%">
	        <c:forEach var="u" items="${users}">
	        <tr class="table-for-users-row">
	       
	        <td style="text-align: center; vertical-align: middle; padding:10px 10px">
			<a href ="<c:url value="users/${u.id}"/>" style="font-size:25px"><c:out value="${u.username}"/></a>
			</td>
            <c:if test="${!empty loggedUser && loggedUser.adminStatus == true}">
	        <td style="text-align: center; vertical-align:middle; padding:10px 10px">
			<form method="POST" action="userSearch">
			<input type="hidden" name="pickedUser" value="${u.username}"/>
			<input type="hidden" name="search" value="${searchTerm}"/>
			<input type="hidden" name="page" value="${current}"/>
			<c:choose>
	       	<c:when test="${u.adminStatus}">
                <input class="btn btn-outline-danger btn-bg not-rounded-bottom" type="submit" value="<spring:message code="user.removeAdmin"/>"/>
            </c:when>
            <c:otherwise>
                <input class="btn btn-outline-success btn-bg not-rounded-bottom" type="submit" value="<spring:message code="user.addAdmin"/>"/>
            </c:otherwise>
            </c:choose>
            </form>
            </td>
            </c:if>
			</tr>
			</c:forEach>
			</table>
			</div>
		</div>
			       </div>
	<div class="col mb-5">
	<div class="row text-center">
			<c:choose>
				<c:when test="${current != 1}">
					<div class="col">
						<form:form name="searchPrev" method="GET" action="userSearch">
							<input type="hidden" name="search" value="${searchTerm}"/>
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
							<form:form name="searchPage" method="GET" action="userSearch">
								<input type="hidden" name="search" value="${searchTerm}"/>
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
						<form:form name="searchNext" method="GET" action="userSearch">
							<input type="hidden" name="search" value="${searchTerm}"/>
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
 </div>
</body>
</html>