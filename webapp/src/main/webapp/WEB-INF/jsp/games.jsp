<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
</head>
<body class="background-primary">
<%@include file="navigation.jsp"%>
<div class="content">
    <div class="game-search-results">
        <c:set var="listName"><spring:message code="search.results" arguments="${searchTerm}"/></c:set>
        <%@ include file="gameList.jsp"%>
    </div>
</div>
</body>
</html>