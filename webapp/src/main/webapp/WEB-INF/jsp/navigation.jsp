<%--
    Include this page:
        <%@ include file="navigation.jsp"%>

    Including jsp should have:
        * inside the header:
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigation.css"/>">

--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    
    <div class="user-container">
	    <c:choose>
	    	<c:when test="${loggedUser == null}">
	    		<a href="<c:url value="/login"/>"><spring:message code="navigation.login"/></a><br>
	    		<a href="<c:url value="/create"/>"><spring:message code="navigation.signup"/></a>
	    	</c:when>
	    	<c:otherwise>
	    		<p>Welcome ${loggedUser.username}</p>
	    		<a href="<c:url value="/logout"/>"><spring:message code="navigation.logout"/></a>
	    	</c:otherwise>
	    </c:choose>
    </div>
        
</nav>