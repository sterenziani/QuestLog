<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/mainGameLists.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameList.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/gameListItem.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/homePage.css"/>">
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
    	<div class="import-prompt-box light-primary-color">
	    	<c:if test="${loggedUser != null && !empty cookieBacklog}">
	    		<p class="import-warning"><spring:message code="index.importPrompt"/></p>
	    		<div class="import-buttons-box">
	    			<a class="button" href="<c:url value="/clear_backlog"/>"><spring:message code="index.noImport"/></a>
		    		<a class="button" href="<c:url value="/transfer_backlog"/>"><spring:message code="index.yesImport"/></a>
		    	</div>
		    	<p class="secondary-text-color import-tip"><spring:message code="index.ignoreImport"/></p>
	    	</c:if>
    	</div>
        <%@include file="mainGameLists.jsp"%>
    </div>
</body>
</html>