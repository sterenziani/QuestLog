<%--
  Include this page:
        <%@ include file="navigationSearchBar.jsp"%>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<div class="form-inline my-2 my-lg-0 d-flex flex-column flex-lg-row align-items-stretch">
<form class="d-flex flex-grow-1" id="form" action="<c:url value="/search"/>">
    <spring:message code="navigation.searchHint" var="searchHint"/>
    <input class="form-control mr-lg-2 flex-lg-grow-1" style="width: max;" type="search" name="search" placeholder="${searchHint}" aria-label="${searchHint}">
    <spring:message code="navigation.search" var="search"/>
    <input type="hidden" value="1" name="page"/>
    <select class="form-control bg-dark text-white border-dark mr-0" name="searchType" id="searchType" onchange="searchTypeFunction()">
		<option value="<c:url value="/search"/>"><spring:message code="game.game"/></option>
		<option value="<c:url value="/userSearch"/>"><spring:message code="search.user"/></option>
	</select>
    <button class="btn btn-dark my-2 my-lg-0" type="submit"><i class="fa fa-search mr-2"></i>${search}</button>
</form>
</div>

<script>
function searchTypeFunction() {
	document.getElementById("form").setAttribute("action",document.getElementById("searchType").value )
}
</script>

