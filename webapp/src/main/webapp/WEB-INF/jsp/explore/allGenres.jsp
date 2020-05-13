<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <spring:message code="explore.genres"/></title>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>    
    <div>
		<%@ include file="genresList.jsp"%>
		<c:url value="/genres" var="listPath"/>
		<%@ include file="../common/pageNumbers.jsp"%>
    </div>
</body>
</html>