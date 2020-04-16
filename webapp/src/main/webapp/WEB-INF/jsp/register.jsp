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
        <h2><spring:message code="register.title"/></h2>
        <h4><spring:message code="register.subtitle"/></h4>
        <c:url value="/create" var="postPath"/>
        <form:form modelAttribute="registerForm" action="${postPath}" method="post">
			<div>
				<form:errors path="username" class="formError" element="p"/>
				<label>
					<spring:message code="registerForm.username"/>
					<spring:message code="registerForm.username.hint" var="usernameHint"/>
					<form:input path="username" type="text" placeholder="${usernameHint}"/>
				</label>
			</div>
			<div>
				<form:errors path="password" class="formError" element="p"/>
				<label>
					<spring:message code="registerForm.password"/>
					<spring:message code="registerForm.password.hint" var="passwordHint"/>
					<form:input path="password" type="password" placeholder="${passwordHint}"/>
				</label>
			</div>
			<div>
				<form:errors path="*" class="formError" element="p"/>
				<form:errors path="repeatPassword" class="formError" element="p"/>
				<label>
					<spring:message code="registerForm.repeatPassword"/>
					<spring:message code="registerForm.repeatPassword.hint" var="repeatPasswordHint"/>
					<form:input path="repeatPassword" type="password" placeholder="${repeatPasswordHint}"/>
				</label>
			</div>
			<div>
				<input type="submit" value="<spring:message code="registerForm.submit"/>"/>
			</div>
		</form:form>
	</div>
</body>
</html>
