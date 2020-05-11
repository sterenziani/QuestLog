<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
    	<div class="logo"><img class="page-header-image" src="${genre.logo}"></img></div>
        <div>
	        <spring:message code="genre.gamesOfGenre" arguments="${genre.name}" var="gamesOfGenre"/>
	        <c:set var="games" value="${gamesInPage}"/>
	        <c:set var="listName" value="${gamesOfGenre}"/>
	        <%@ include file="gameList.jsp"%>
	        <c:url value="/genres/${genre.id}" var="listPath"/>
	        <c:set var="path" value="${gamesInPage}"/>
	       	<%@ include file="pageNumbers.jsp"%>
    	</div>

    </div>
</body>
</html>