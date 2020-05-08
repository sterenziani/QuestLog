<%--
  Include this page:
        <%@ include file="navigationSearchBar.jsp"%>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="navigation-search-bar">
    <form action="<c:url value="/search"/>">
        <div class="navigation-search-bar-content">
            <div class="navigation-search-bar-input">
            	<spring:message code="navigation.searchHint" var="searchHint"/>
                <input class="navigation-search-text" type="text" name="search" placeholder="${searchHint}">
            </div>
            <%-- <a class="navigation-search-button" href="#">

            </a>--%>
            <div class="navigation-search-bar-button">
            	<spring:message code="navigation.search" var="search"/>
                <input type="submit" value="${search}"/>
            </div>
        </div>
    </form>
</div>
