<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog</title>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="content">
	    <c:if test="${loggedUser != null && !empty cookieBacklog}">
		    <div class="container text-center align-middle mt-3">
		    	<div class="my-1 py-4 bg-light border-bottom border-primary rounded-lg">
	    		<p class="import-warning"><spring:message code="index.importPrompt"/></p>
	    		<div>
	    			<a class="btn btn-dark" href="<c:url value="/backlog/clear"/>"><spring:message code="index.noImport"/></a>
		    		<a class="btn btn-dark" href="<c:url value="/backlog/transfer"/>"><spring:message code="index.yesImport"/></a>
		    	</div>
		    	<p class="my-1"><spring:message code="index.ignoreImport"/></p>
		        </div>
		    </div>
	    </c:if>
        <%@include file="mainGameLists.jsp"%>
    </div>
</body>
</html>