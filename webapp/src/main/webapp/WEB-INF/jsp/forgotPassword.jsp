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
				<c:choose>
			    	<c:when test="${emailSent}">
					    <div class="container text-center align-middle px-5">
						        <p><spring:message code="forgotPassword.emailSent"/></p>
					    </div>
			    	</c:when>
			    	<c:otherwise>
			    		<div class="content user-form">
			    			<h2 class="share-tech-mono"><spring:message code="forgotPassword.title"/></h2>
			        		<p><spring:message code="forgotPassword.subtitle"/></p>
					        <c:url value="/forgotPassword" var="postPath"/>
					        <form:form modelAttribute="forgotPasswordForm" action="${postPath}" method="post">
								<div class="form-field form-field mt-3">
									<form:errors path="email" class="form-error" element="p"/>
									<label>
										<spring:message code="registerForm.email"/>
										<spring:message code="registerForm.email.hint" var="emailHint"/>
										<form:input class="form-control" path="email" placeholder="${emailHint}"/>
									</label>
								</div>
								<div class="form-field form-field mt-3">
									<input type="submit" class="btn btn-dark" value="<spring:message code="forgotPassword.submit"/>"/>
								</div>
							</form:form>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
</body>
</html>