<%--
    Include this page:
        <%@ include file="navigation.jsp"%>

    Including jsp should have:
        * inside the header:
            ** <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigation.css"/>">

--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav id="navigation">
    <div class="navigation-logo-container">
        <a href="<c:url value="/"/>">
            <div class="navigation-logo">
                <h1>QuestLog</h1>
            </div>
        </a>
    </div>
    <div class="navigation-search">
        <%@include file="navigationSearchBar.jsp"%>
    </div>
</nav>