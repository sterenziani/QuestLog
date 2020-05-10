<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <%@include file="commonHead.jsp"%>
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <c:set var="publisherEndIndex" value="${listSize}"/>
    <div>
		<%@ include file="publishersList.jsp"%>
    </div>
</body>
</html>