<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
        <div>
	        <c:forEach var="user" items="${users}">
			<a href ="<c:url value="user/${user.id}"/>" /></option>
			</c:forEach>
	       	<%@ include file="pageNumbers.jsp"%>
    	</div>

    </div>
</body>
</html>