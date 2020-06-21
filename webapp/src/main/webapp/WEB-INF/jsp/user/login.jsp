<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
   <%@include file="../common/commonHead.jsp"%>
   <title>QuestLog - <spring:message code="login.title"/></title>
</head>
<body class="background-color">
    <%@include file="../common/navigation.jsp"%>
    <div class="content user-form">
        <c:url value="login" var="postPath"/>
	    <div class="container text-center align-middle">
	    	<div class="my-5 py-5 bg-light border-bottom border-primary rounded-lg">
        		<h2 class="share-tech-mono"><spring:message code="login.title"/></h2>
        		<c:if test="${error}"><p class="form-error"><spring:message code="login.invalid"/></p></c:if>
		        <form:form modelAttribute="login" action="${postPath}" method="post">
					<div class="form-field mt-3">
						<label>
							<spring:message code="registerForm.username"/>
							<spring:message code="registerForm.username.hint" var="usernameHint"/>
							<input class="form-control" name="username" type="text" placeholder="${usernameHint}"/>
						</label>
					</div>
					<div class="form-field mt-2">
						<label>
							<spring:message code="registerForm.password"/>
							<spring:message code="registerForm.password.hint" var="passwordHint"/>
							<input class="form-control" name="password" type="password" placeholder="${passwordHint}"/>
						</label>
					</div>
					<div class="form-field form-checkbox mt-2">
						<input class="form-check-input" name="rememberme" type="checkbox"/>
						<label class="form-check-label"><spring:message code="loginForm.rememberme"/></label>
					</div>
					<div class="form-field mt-3">
						<input type="submit" class="btn btn-dark" value="<spring:message code="loginForm.submit"/>"/>
					</div>
				</form:form>
				<div class="form-field mt-3">
					<a class="default-primary-color" href="<c:url value="/forgotPassword"/>"><spring:message code="login.forgotPassword"/></a>
				</div>
	        </div>
	    </div>
	</div>
</body>
</html>