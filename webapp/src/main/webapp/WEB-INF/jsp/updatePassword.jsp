<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
   <%@include file="commonHead.jsp"%>
</head>
<body class="background-color">
    <%@include file="navigation.jsp"%>
    <div class="content user-form">
    	<h2><spring:message code="updatePassword.title"/></h2>
        <h4><spring:message code="updatePassword.resetPasswords"/></h4>
        <c:url value="/changePassword" var="postPath"/>
        <form:form modelAttribute="changePasswordForm" action="${postPath}" method="post">
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
			<div style="display:none">
				<label><form:input path="token"/></label>
			</div>
			<div class="form-field">
				<input type="submit" class="button" value="<spring:message code="updatePassword.submit"/>"/>
			</div>
		</form:form>
	</div>
</body>
</html>