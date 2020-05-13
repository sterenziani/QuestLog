<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>
    <div>
        <div>
        	<c:set var="seeAllGenresUrl" value="/genres"/>
	        <%@ include file="genresList.jsp"%>
    	</div>
        <div>
        	<c:set var="seeAllPlatformsUrl" value="/platforms"/>
	        <%@ include file="platformsList.jsp"%>
    	</div>
    	<div>
    	    <c:set var="seeAllDevsUrl" value="/developers"/>
	        <%@ include file="developersList.jsp"%>
    	</div>
    	<div>
    		<c:set var="seeAllPubsUrl" value="/publishers"/>
	        <%@ include file="publishersList.jsp"%>
    	</div>
    </div>
</body>
</html>