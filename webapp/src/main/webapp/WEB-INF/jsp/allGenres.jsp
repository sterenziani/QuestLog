<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <c:set var="genreEndIndex" value="${listSize}"/>
    
    <div>
		<%@ include file="genresList.jsp"%>
    </div>
</body>
</html>