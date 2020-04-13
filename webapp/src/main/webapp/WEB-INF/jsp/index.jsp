<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>QuestLog</title>

    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigation.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigationSearchBar.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
</head>
<body class="background-primary">
    <%--<div class="search-container">
        <form action="<c:url value="/search"/> ">
            <input type="text" placeholder="Search..." name="search" id="search">
        </form>
    </div>--%>
    <%@include file="navigation.jsp"%>
    <div class="content">
        <%@include file="mainGameLists.jsp"%>
    </div>
</body>
</html>
