<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>LIST OF GENRES</h2>
<br><br>
<c:forEach items="${genres}" var="genre">
	<div class="game-list-item">
	    <a href="<c:url value="/genres/${genre.id}"/>">
	        <div class="game-list-item-content">
	            <div class="game-list-item-cover">
	                <img src="<c:url value="${genre.logo}"/>" alt="${genre.name}"/>
	            </div>
	            <div class="game-list-item-data">
	                <h3><c:out value="${genre.name}"/></h3>
	            </div>
	        </div>
	    </a>
	 </div>
</c:forEach>
</body>
</html>