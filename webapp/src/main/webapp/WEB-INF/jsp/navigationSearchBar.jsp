<%--
  Include this page:
        <%@ include file="navigationSearchBar.jsp"%>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form class="form-inline my-2 my-lg-0 d-flex flex-column flex-lg-row align-items-stretch" action="<c:url value="/search"/>">
    <spring:message code="navigation.searchHint" var="searchHint"/>
    <input class="form-control mr-lg-2 flex-lg-grow-1" type="search" name="search" placeholder="${searchHint}" aria-label="${searchHint}">
    <spring:message code="navigation.search" var="search"/>
    <button class="btn btn-dark my-2 my-lg-0" type="submit"><i class="fa fa-search mr-2"></i>${search}</button>
</form>
