<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <spring:message code="explore.publishers"/></title>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>
    <div>
		<%@ include file="publishersList.jsp"%>
		<c:url value="/publishers" var="listPath"/>
		<%@ include file="../common/pageNumbers.jsp"%>
    </div>
</body>
</html>