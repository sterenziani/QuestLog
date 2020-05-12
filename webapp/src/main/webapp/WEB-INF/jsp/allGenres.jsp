<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>    
    <div>
		<%@ include file="genresList.jsp"%>
		<c:url value="/genres" var="listPath"/>
		<%@ include file="pageNumbers.jsp"%>
    </div>
</body>
</html>