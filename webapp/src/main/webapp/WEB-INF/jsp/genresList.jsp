<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<div class="explore-list">
	<div class="explore-list-header">
		<h2><spring:message code="explore.genres"/></h2>
	</div>
	<div class="explore-list-items">
		<c:forEach items="${genres}" var="genre">
			<div class="explore-list-item">
			    <a href="<c:url value="genres/${genre.id}"/>">
			        <div>
			            <div class="explore-list-item-icon">
			                <img src="<c:url value="${genre.logo}"/>" alt="${genre.name}"/>
			            </div>
			            <div class="explore-list-item-data">
			                <h3><c:out value="${genre.name}"/></h3>
			            </div>
			        </div>
			    </a>
			 </div>
		</c:forEach>
	</div>
</div>
</body>
</html>