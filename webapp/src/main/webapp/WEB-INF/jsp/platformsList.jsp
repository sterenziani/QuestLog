<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>LIST OF PLATFORMS</h2>
<br><br>
<c:forEach items="${platforms}" var="platform">
	<div class="game-list-item">
	    <a href="<c:url value="/platforms/${platform.id}"/>">
	        <div class="game-list-item-content">
	            <div class="game-list-item-cover">
	                <img src="<c:url value="${platform.logo}"/>" alt="${platform.name}"/>
	            </div>
	            <div class="game-list-item-data">
	                <h3><c:out value="${platform.name}"/></h3>
	            </div>
	        </div>
	    </a>
	 </div>
</c:forEach>
</body>
</html>