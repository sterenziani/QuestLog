<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
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