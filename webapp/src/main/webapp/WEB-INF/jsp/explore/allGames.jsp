<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@ include file="../common/commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
        <div class="all-games">
	        <spring:message code="index.allGames" var="allGames"/>
	        <c:set var="listName" value="${allGames}"/>
	        <%@ include file="../common/gameList.jsp"%>
    	</div>
    </div>
</body>
</html>