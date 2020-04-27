<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>LIST OF PUBLISHERS</h2>
<br><br>
<c:forEach items="${publishers}" var="publisher">    
	<div class="game-list-item">
	    <a href="<c:url value="/publishers/${publisher.id}"/>">
	        <div class="game-list-item-content">
	            <div class="game-list-item-cover">
	                <img src="<c:url value="${publisher.logo}"/>" alt="${publisher.name}"/>
	            </div>
	            <div class="game-list-item-data">
	                <h3><c:out value="${publisher.name}"/></h3>
	            </div>
	        </div>
	    </a>
	 </div>
</c:forEach>
</body>
</html>