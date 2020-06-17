<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
   <%@include file="../common/commonHead.jsp"%>
   <title>QuestLog - <spring:message code="error.title"/></title>
</head>
<body class="background-color">
	<nav class="navbar navbar-expand-lg navbar-dark bg-primary">
	    <a class="navbar-brand" href="<c:url value="/"/>">
	        <div class="navigation-logo">
	            <h1><img src="<c:url value="/images/static/questlog-logo.png"/>" alt="<spring:message code="navigation.questLog"/>"/></h1>
	        </div>
	    </a>
	    <div class="w-100" id="navbarSupportedContent">
	        <ul class="navbar-nav d-flex w-100">
	            <li class="nav-item flex-grow-1 ml-lg-3">
	                <div class="nav-link w-100">
	                    <%@include file="../common/navigationSearchBar.jsp"%>
	                </div>
	            </li>
	            <li class="nav-item">
	                <div class="nav-link">
	                    <a class="btn btn-dark w-100" role="button" href="<c:url value="/explore"/>"><spring:message code="navigation.explore"/></a>
	                </div>
	            </li>
	        </ul>
	    </div>
	</nav>
    <div class="container text-center align-middle">
    	<div class="my-5 py-5 bg-light border-bottom border-primary rounded-lg">
	        <h2 class="align-middle"><spring:message code="error.title"/></h2>
	        <h5 class="align-middle"><spring:message code="${msg}"/></h5>
        </div>
    </div>
</body>
</html>