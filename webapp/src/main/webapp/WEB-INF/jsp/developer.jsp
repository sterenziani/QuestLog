<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
        <div>
	        <spring:message code="developer.gamesFromDeveloper" arguments="${developer.name}" var="gamesFromDeveloper"/>
	        <c:set var="games" value="${gamesInPage}"/>
	        <c:set var="listName" value="${gamesFromDeveloper}"/>
	        <%@ include file="gameList.jsp"%>
	        <c:url value="/developers/${developer.id}" var="listPath"/>
	        <c:set var="path" value="${gamesInPage}"/>
	        <%@ include file="pageNumbers.jsp"%>
    	</div>
    </div>
</body>
</html>