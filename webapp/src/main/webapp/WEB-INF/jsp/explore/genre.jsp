<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <spring:message code="genres.${genre.name}" arguments="${genre.name}"/></title>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
        <div>
        	<spring:message code="genres.${genre.name}" arguments="${genre.name}" var="genreName"/>
	        <spring:message code="genre.gamesOfGenre" arguments="${genreName}" var="gamesOfGenre"/>
	        <c:set var="games" value="${gamesInPage}"/>
	        <c:set var="listName" value="${gamesOfGenre}"/>
	        <%@ include file="../common/gameList.jsp"%>
	        <c:url value="/genres/${genre.id}" var="listPath"/>
	        <c:set var="path" value="${gamesInPage}"/>
	       	<%@ include file="../common/pageNumbers.jsp"%>
    	</div>
    </div>
</body>
</html>