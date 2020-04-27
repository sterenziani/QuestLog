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
        <h2><spring:message code="login.title"/></h2>
        <c:if test="${error}"><p class="form-error"><spring:message code="login.invalid"/></p></c:if>
        <c:url value="login" var="postPath"/>
        <form:form modelAttribute="login" action="${postPath}" method="post">
			<div class="form-field">
				<label>
					<spring:message code="registerForm.username"/>
					<spring:message code="registerForm.username.hint" var="usernameHint"/>
					<input name="username" type="text" placeholder="${usernameHint}"/>
				</label>
			</div>
			<div class="form-field">
				<label>
					<spring:message code="registerForm.password"/>
					<spring:message code="registerForm.password.hint" var="passwordHint"/>
					<input name="password" type="password" placeholder="${passwordHint}"/>
				</label>
			</div>
			<div class="form-field form-checkbox">
				<label>
					<input name="rememberme" type="checkbox"/>
					<spring:message code="loginForm.rememberme"/>
				</label>
			</div>
			<div class="form-field">
				<input type="submit" class="button" value="<spring:message code="loginForm.submit"/>"/>
			</div>
		</form:form>
	</div>
</body>
</html>