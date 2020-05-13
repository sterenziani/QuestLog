<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
        <div>
	        <spring:message code="publisher.gamesFromPublisher" arguments="${publisher.name}" var="gamesFromPublisher"/>
	        <c:set var="games" value="${gamesInPage}"/>
	        <c:set var="listName" value="${gamesFromPublisher}"/>
	        <%@ include file="../common/gameList.jsp"%>
	        <c:url value="/publishers/${publisher.id}" var="listPath"/>
	        <c:set var="path" value="${gamesInPage}"/>
	        <%@ include file="../common/pageNumbers.jsp"%>
    	</div>
    </div>
</body>
</html>