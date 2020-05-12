<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
    <div class="card m-5 bg-very-light right-wave left-wave">
          <div class="card-header bg-very-dark text-white d-flex">
         <div>
         <h2 class="share-tech-mono"><spring:message code="search.results" arguments="${searchTerm}"/></h2>
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
			<a href ="<c:url value="users/${u.id}"/>" style="font-size:25px">${u.username}</a>
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
	       	<%@ include file="pageNumbers.jsp"%>

	    </div>
</body>
</html>