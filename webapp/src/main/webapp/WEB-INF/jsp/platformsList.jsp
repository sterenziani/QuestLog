<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<div class="explore-list">
	<div class="explore-list-header">
		<h2><spring:message code="explore.platforms"/></h2>
		<a href="<c:url value="/platforms"/>"><spring:message code="explore.seeAll"/></a>
	</div>
	<div class="explore-list-items">
		<c:forEach items="${platforms}" var="platform" end="${platformEndIndex}">
			<div class="explore-list-item">
			    <a href="<c:url value="/platforms/${platform.id}"/>">
			        <div>
			            <div class="explore-list-item-icon">
			                <img height="50px" src="<c:url value="${platform.logo}"/>" alt="${platform.name}"/>
			            </div>
			            <div class="explore-list-item-data">
			                <h3><c:out value="${platform.name}"/></h3>
			            </div>
			        </div>
			    </a>
			 </div>
		</c:forEach>
	</div>
</div>
</body>
</html>