<%--
  Include this page:
        <%@ include file="navigationSearchBar.jsp"%>

    Including jsp should have:
        * variable title
        * variable items
        * inside the header:
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigationSearchBar.css"/>">
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="navigation-search-bar">
    <form action="<c:url value="/searchFilter"/>">
        <div class="navigation-search-bar-content">
            <div class="navigation-search-bar-input">
            	<spring:message code="navigation.searchHint" var="searchHint"/>
                <input class="navigation-search-text" type="text" name="search" placeholder="${searchHint}">
            </div>
            <%-- <a class="navigation-search-button" href="#">

            </a>--%>
            <div class="navigation-search-bar-button">
            	<spring:message code="navigation.search" var="search"/>
            	<input type="hidden" value="1" name="page"/>
                <input type="submit" value="${search}"/>
            </div>
        </div>
    </form>
</div>
