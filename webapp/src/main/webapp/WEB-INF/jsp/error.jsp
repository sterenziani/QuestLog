<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
   <%@include file="commonHead.jsp"%>
</head>
<body class="background-color">
	<nav id="navigation" class="default-primary-color">
	    <div class="navigation-logo-container">
	        <a href="<c:url value="/"/>">
	            <div class="navigation-logo">
	                <h1><spring:message code="navigation.questLog"/></h1>
	            </div>
	        </a>
	    </div>
	    <div class="navigation-search">
	        <%@include file="navigationSearchBar.jsp"%>
	    </div>

	    <div class="navigation-explore-container">
	        <a class="button" href="<c:url value="/explore"/>"><spring:message code="navigation.explore"/></a>
	    </div>
	</nav>
    <div class="error-box">
        <h2><spring:message code="error.title"/></h2>
        <h5><spring:message code="${msg}"/></h5>
    </div>
</body>
</html>