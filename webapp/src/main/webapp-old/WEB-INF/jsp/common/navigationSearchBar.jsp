<%--
  Include this page:
        <%@ include file="navigationSearchBar.jsp"%>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="form-inline my-2 my-lg-0 d-flex flex-column flex-lg-row align-items-stretch flex-grow-1">
<form class="d-flex flex-grow-1 flex-lg-row flex-column" id="search-form" action="<c:url value="/search"/>">
    <div class="mr-lg-2 flex-lg-grow-1 d-flex flex-column">
        <spring:message code="navigation.searchHint" var="searchHint"/>
        <input class="form-control flex-grow-1" type="search" name="search" placeholder="${searchHint}" aria-label="${searchHint}">
    </div>
    <spring:message code="navigation.search" var="search"/>
    <input type="hidden" value="1" name="page"/>
    <div class="d-flex mr-0 mr-lg-2 flex-column">
	    <select class="form-control bg-dark text-white border-dark flex-grow-1" name="searchType" id="searchType-nav" onchange="searchTypeFunction()">
			<option value="<c:url value="/search"/>"><spring:message code="game.game"/></option>
			<option value="<c:url value="/userSearch"/>"><spring:message code="search.user"/></option>
		</select>
    </div>
    <div class="d-flex">
        <button class="btn btn-dark my-2 my-lg-0 flex-grow-1" type="submit"><i class="fa fa-search mr-2"></i><c:out value="${search}"/></button>
    </div>
</form>
</div>

<script>
function searchTypeFunction() {
	document.getElementById("search-form").setAttribute("action",document.getElementById("searchType-nav").value )
}
</script>

