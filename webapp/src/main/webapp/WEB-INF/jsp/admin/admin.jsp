<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <%@include file="../commonHead.jsp"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/game.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameDetails.css"/>">
   <link rel="stylesheet" type="text/css" href="<c:url value="/css/userForm.css"/>">
</head>
<body>
    <%@ include file="../navigation.jsp"%>
	<div class="content">
		You are admin!
    </div>
</body>
</html>