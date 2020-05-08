<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div>
        <div>
	        <%@ include file="genresList.jsp"%>
    	</div>
    	<c:set var="platformEndIndex" value="17"/>
        <div>
	        <%@ include file="platformsList.jsp"%>
    	</div>
    	<c:set var="developerEndIndex" value="17"/>
    	<div>
	        <%@ include file="developersList.jsp"%>
    	</div>
    	<c:set var="publisherEndIndex" value="17"/>
    	<div>
	        <%@ include file="publishersList.jsp"%>
    	</div>
    </div>
</body>
</html>