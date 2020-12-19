<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>
    <title>QuestLog - <c:out value="${publisher.name}"/></title>
    <div class="content">
        <div>
	        <spring:message code="publisher.gamesFromPublisher" arguments="${publisher.name}" var="gamesFromPublisher"/>
	        <c:set var="games" value="${gamesInPage}"/>
	        <c:set var="listName" value="${gamesFromPublisher}"/>
	        <%@ include file="../common/gameList.jsp"%>
	        <c:url value="/publishers/${publisher.id}" var="listPath"/>
	        <c:set var="path" value="${gamesInPage}"/>
	        <%@ include file="../common/pageNumbers.jsp"%>
    	</div>
    </div>
</body>
</html>