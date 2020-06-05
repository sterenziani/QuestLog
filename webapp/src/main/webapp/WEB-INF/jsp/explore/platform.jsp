<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <c:out value="${platform.name}"/></title>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
        <div>
	        <spring:message code="platform.gamesForPlatform" arguments="${platform.name}" var="gamesForPlatform"/>
	        <c:set var="games" value="${gamesInPage}"/>
	        <c:set var="listName" value="${gamesForPlatform}"/>
	        <%@ include file="../common/gameList.jsp"%>
	        <c:url value="/platforms/${platform.id}" var="listPath"/>
	        <c:set var="path" value="${gamesInPage}"/>
	        <%@ include file="../common/pageNumbers.jsp"%>
    	</div>
    </div>
</body>
</html>