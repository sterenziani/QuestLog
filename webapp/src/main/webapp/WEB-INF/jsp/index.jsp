<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>QuestLog</title>
    <div class="search-container">
    <form action="/webapp/search">
      <input type="text" placeholder="Search..." name="search" id="search">
    </form>
    </div>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
</head>
<body>
    <%@include file="navigation.jsp"%>
    <div class="content">
        <%@include file="mainGameLists.jsp"%>
    </div>
</body>
</html>
