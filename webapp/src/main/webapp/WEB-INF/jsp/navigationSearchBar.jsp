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
    <form action="<c:url value="/search"/>">
        <div class="navigation-search-bar-content">
            <div class="navigation-search-bar-input">
                <input class="navigation-search-text" type="text" name="search" placeholder="Search for a game...">
            </div>
            <%-- <a class="navigation-search-button" href="#">

            </a>--%>
            <div class="navigation-search-bar-button">
                <input type="submit" value="Search"/>
            </div>
        </div>
    </form>
</div>
