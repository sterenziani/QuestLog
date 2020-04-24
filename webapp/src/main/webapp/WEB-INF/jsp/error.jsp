<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>QuestLog</title>
    <link rel="stylesheet" type="text/css" href="<c:out value="http://fonts.googleapis.com/css?family=Roboto"/>" >
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigation.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigationSearchBar.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/error.css"/>">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="error-box">
        <h2>Whoops!</h2>
        <h5>${msg}</h5>
    </div>
</body>
</html>