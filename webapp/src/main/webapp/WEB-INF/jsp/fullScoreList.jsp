<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body>
    <%@include file="navigation.jsp"%>
    <div class="content">
	    <%@ include file="scoresList.jsp"%>
	    <%@ include file="pageNumbers.jsp"%>
	</div>
</body>
</html>