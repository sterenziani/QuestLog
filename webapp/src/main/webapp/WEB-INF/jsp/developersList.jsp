<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>LIST OF DEVELOPERS</h2>
<br><br>
<c:forEach items="${developers}" var="developer">     
	<div class="game-list-item">
	    <a href="<c:url value="/developers/${developer.id}"/>">
	        <div class="game-list-item-content">
	            <div class="game-list-item-cover">
	                <img src="<c:url value="${developer.logo}"/>" alt="${developer.name}"/>
	            </div>
	            <div class="game-list-item-data">
	                <h3><c:out value="${developer.name}"/></h3>
	            </div>
	        </div>
	    </a>
	 </div>
</c:forEach>
</body>
</html>