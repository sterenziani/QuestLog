<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
    <title>QuestLog</title>
    <link rel="stylesheet" type="text/css" href="<c:out value="http://fonts.googleapis.com/css?family=Roboto"/>" >
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/style.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigation.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/navigationSearchBar.css"/>">
    <link rel="stylesheet" type="text/css" href="<c:url value="/css/error.css"/>">
</head>
<body class="background-primary">
    <%@include file="navigation.jsp"%>
    <div class="content">
        <h2><spring:message code="login.title"/></h2>
        <c:if test="${error}"><p class="form-error"><spring:message code="login.invalid"/></p></c:if>
        <form:form modelAttribute="login" action="${postPath}" method="post">
			<div>
				<label>
					<spring:message code="registerForm.username"/>
					<spring:message code="registerForm.username.hint" var="usernameHint"/>
					<input name="username" type="text" placeholder="${usernameHint}"/>
				</label>
			</div>
			<div>
				<label>
					<spring:message code="registerForm.password"/>
					<spring:message code="registerForm.password.hint" var="passwordHint"/>
					<input name="password" type="password" placeholder="${passwordHint}"/>
				</label>
			</div>
			<div>
				<label>
					<input name="rememberme" type="checkbox"/>
					<spring:message code="loginForm.rememberme"/>
				</label>
			</div>
			<div>
				<input type="submit" value="<spring:message code="loginForm.submit"/>"/>
			</div>
		</form:form>
	</div>
</body>
</html>