<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<html>
<head>
   <%@include file="commonHead.jsp"%>
</head>
<body class="background-color">
    <%@include file="navigation.jsp"%>
	<c:choose>
    	<c:when test="${emailSent}">
		    <div class="container text-center align-middle px-5">
		    	<div class="my-5 py-5 bg-light border-bottom border-primary rounded-lg">
			        <h4><spring:message code="forgotPassword.emailSent"/></h4>
		        </div>
		    </div>
    	</c:when>
    	<c:otherwise>
    		<div class="content user-form">
    			<h2><spring:message code="forgotPassword.title"/></h2>
        		<h4><spring:message code="forgotPassword.subtitle"/></h4>
		        <c:url value="/forgotPassword" var="postPath"/>
		        <form:form modelAttribute="forgotPasswordForm" action="${postPath}" method="post">
					<div class="form-field">
						<form:errors path="email" class="form-error" element="p"/>
						<label>
							<spring:message code="registerForm.email"/>
							<spring:message code="registerForm.email.hint" var="emailHint"/>
							<form:input path="email" placeholder="${emailHint}"/>
						</label>
					</div>
					<div class="form-field">
						<input type="submit" class="button" value="<spring:message code="forgotPassword.submit"/>"/>
					</div>
				</form:form>
			</div>
		</c:otherwise>
	</c:choose>
</body>
</html>