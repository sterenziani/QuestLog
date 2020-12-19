<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <c:out value="${developer.name}"></c:out></title>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
        <div>
	        <spring:message code="developer.gamesFromDeveloper" arguments="${developer.name}" var="gamesFromDeveloper"/>
	        <c:set var="games" value="${gamesInPage}"/>
	        <c:set var="listName" value="${gamesFromDeveloper}"/>
	        <%@ include file="../common/gameList.jsp"%>
	        <c:url value="/developers/${developer.id}" var="listPath"/>
	        <c:set var="path" value="${gamesInPage}"/>
	        <%@ include file="../common/pageNumbers.jsp"%>
    	</div>
    </div>
</body>
</html>