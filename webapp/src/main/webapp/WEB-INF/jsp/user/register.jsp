<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
   <%@include file="../common/commonHead.jsp"%>
   <title>QuestLog - <spring:message code="navigation.signup"/></title>
</head>
<body class="background-color">
    <%@include file="../common/navigation.jsp"%>
    <div class="content user-form">
        <c:url value="login" var="postPath"/>
	    <div class="container text-center align-middle">
	    	<div class="my-5 py-5 bg-light border-bottom border-primary rounded-lg">
		        <h2 class="share-tech-mono"><spring:message code="navigation.signup"/></h2>
		        <c:url value="/create" var="postPath"/>
		        <form:form modelAttribute="registerForm" action="${postPath}" method="post">
					<div class="form-field mt-3">
						<form:errors path="username" class="form-error" element="p"/>
						<label>
							<spring:message code="registerForm.username"/>
							<spring:message code="registerForm.username.hint" var="usernameHint"/>
							<form:input class="form-control" path="username" type="text" placeholder="${usernameHint}"/>
						</label>
					</div>
					<div class="form-field form-field mt-2">
						<form:errors path="password" class="form-error" element="p"/>
						<label>
							<spring:message code="registerForm.password"/>
							<spring:message code="registerForm.password.hint" var="passwordHint"/>
							<form:input class="form-control" path="password" type="password" placeholder="${passwordHint}"/>
						</label>
					</div>
					<div class="form-field form-field mt-2">
						<form:errors class="form-error" element="p"/>
						<form:errors path="repeatPassword" class="form-error" element="p"/>
						<label>
							<spring:message code="registerForm.repeatPassword"/>
							<spring:message code="registerForm.repeatPassword.hint" var="repeatPasswordHint"/>
							<form:input class="form-control" path="repeatPassword" type="password" placeholder="${repeatPasswordHint}"/>
						</label>
					</div>
					<div class="form-field form-field mt-2">
						<form:errors path="email" class="form-error" element="p"/>
						<label>
							<spring:message code="registerForm.email"/>
							<spring:message code="registerForm.email.hint" var="emailHint"/>
							<form:input class="form-control" path="email" placeholder="${emailHint}"/>
						</label>
					</div>
					<div class="form-field form-field mt-3">
						<input type="submit" class="btn btn-dark" value="<spring:message code="registerForm.submit"/>"/>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</body>
</html>
