<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
   <%@include file="commonHead.jsp"%>
   <link rel="stylesheet" type="text/css" href="<c:url value="/css/error.css"/>">
   <link rel="stylesheet" type="text/css" href="<c:url value="/css/userForm.css"/>">
</head>
<body class="background-color">
    <%@include file="navigation.jsp"%>
    <div class="content user-form">
        <h2><spring:message code="register.title"/></h2>
        <h4><spring:message code="register.subtitle"/></h4>
        <c:url value="/create" var="postPath"/>
        <form:form modelAttribute="registerForm" action="${postPath}" method="post">
			<div class="form-field">
				<form:errors path="username" class="form-error" element="p"/>
				<label>
					<spring:message code="registerForm.username"/>
					<spring:message code="registerForm.username.hint" var="usernameHint"/>
					<form:input path="username" type="text" placeholder="${usernameHint}"/>
				</label>
			</div>
			<div class="form-field">
				<form:errors path="password" class="form-error" element="p"/>
				<label>
					<spring:message code="registerForm.password"/>
					<spring:message code="registerForm.password.hint" var="passwordHint"/>
					<form:input path="password" type="password" placeholder="${passwordHint}"/>
				</label>
			</div>
			<div class="form-field">
				<form:errors class="form-error" element="p"/>
				<form:errors path="repeatPassword" class="form-error" element="p"/>
				<label>
					<spring:message code="registerForm.repeatPassword"/>
					<spring:message code="registerForm.repeatPassword.hint" var="repeatPasswordHint"/>
					<form:input path="repeatPassword" type="password" placeholder="${repeatPasswordHint}"/>
				</label>
			</div>
			<div class="form-field">
				<form:errors path="email" class="form-error" element="p"/>
				<label>
					<spring:message code="registerForm.email"/>
					<spring:message code="registerForm.email.hint" var="emailHint"/>
					<form:input path="email" type="email" placeholder="${emailHint}"/>
				</label>
			</div>
			<div class="form-field">
				<input type="submit" class="button" value="<spring:message code="registerForm.submit"/>"/>
			</div>
		</form:form>
	</div>
</body>
</html>
