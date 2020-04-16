<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<html>
<body>
<h2><spring:message code="user.introduction" arguments="${user.username}"/></h2>
</body>
</html>
