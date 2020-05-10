<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<div class="explore-list">
	<div class="explore-list-header">
		<h2><spring:message code="explore.developers"/></h2>
		<a href="<c:url value="/developers"/>"><spring:message code="explore.seeAll"/></a>
	</div>
	<div class="explore-list-items">
		<c:forEach items="${developers}" var="developer" end="${developerEndIndex}">
			<div class="explore-list-item">
			    <a href="<c:url value="/developers/${developer.id}"/>">
			        <div class="explore-list-item-dev-pub">
			            <div class="explore-list-item-data">
			                <h3><c:out value="${developer.name}"/></h3>
			            </div>
			        </div>
			    </a>
			 </div>
		</c:forEach>
	</div>
</div>
</body>
</html>