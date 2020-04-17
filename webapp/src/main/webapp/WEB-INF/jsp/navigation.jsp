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
<nav id="navigation">
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