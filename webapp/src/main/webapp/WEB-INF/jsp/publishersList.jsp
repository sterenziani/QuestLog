<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>LIST OF PUBLISHERS</h2>
<br><br>
<c:forEach items="${publishers}" var="publisher">    
        <h2>[${publisher.id}] ${publisher.name}</h2>
        <br><img height="70" src=${publisher.logo}></img>
        <h4>Published games:</h4>
		<c:forEach items="${publisher.games}" var="game">
		    <li>      
		        [${game.id}] ${game.title}
		    </li>
		    <br>
		</c:forEach>
    	<br>
</c:forEach>
</body>
</html>