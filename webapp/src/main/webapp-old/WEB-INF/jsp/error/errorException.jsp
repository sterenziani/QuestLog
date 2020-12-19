<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
   <%@include file="../common/commonHead.jsp"%>
   <title>QuestLog - <spring:message code="error.title"/></title>
</head>
<body class="background-color">
    <%@include file="../common/navigation.jsp"%>
    <div class="container text-center align-middle">
    	<div class="my-5 py-5 bg-light border-bottom border-primary rounded-lg">
	        <h2 class="align-middle"><spring:message code="error.title"/></h2>
	        <h5 class="align-middle"><spring:message code="${msg}"/></h5>
        </div>
    </div>
</body>
</html>