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
        <c:url value="login" var="postPath"/>
	    <div class="container text-center align-middle">
	    	<div class="my-5 py-5 bg-light border-bottom border-primary rounded-lg">
    			<h2 class="share-tech-mono"><spring:message code="updatePassword.title"/></h2>
        		<p><spring:message code="updatePassword.resetPasswords"/></p>
        		<c:url value="/changePassword" var="postPath"/>
		        <form:form modelAttribute="changePasswordForm" action="${postPath}" method="post">
					<div class="form-field mt-3">
						<form:errors path="password" class="form-error" element="p"/>
						<label>
							<spring:message code="registerForm.password"/>
							<spring:message code="registerForm.password.hint" var="passwordHint"/>
							<form:input class="form-control" path="password" type="password" placeholder="${passwordHint}"/>
						</label>
					</div>
					<div class="form-field mt-2">
						<form:errors class="form-error" element="p"/>
						<form:errors path="repeatPassword" class="form-error" element="p"/>
						<label>
							<spring:message code="registerForm.repeatPassword"/>
							<spring:message code="registerForm.repeatPassword.hint" var="repeatPasswordHint"/>
							<form:input class="form-control" path="repeatPassword" type="password" placeholder="${repeatPasswordHint}"/>
						</label>
					</div>
					<div style="display:none">
						<label><form:input path="token"/></label>
					</div>
					<div class="form-field mt-3">
						<input type="submit" class="btn btn-dark" value="<spring:message code="updatePassword.submit"/>"/>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>