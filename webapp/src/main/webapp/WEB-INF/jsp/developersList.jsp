<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1>LIST OF DEVELOPERS</h1>
<br><br>
<c:forEach items="${developers}" var="developer">     
        <h3>[${developer.id}] ${developer.name}</h3>
        <br><img height="70" src=${developer.logo}></img>
        <h5>Known for:</h5>
        <c:forEach items="${developer.games}" var="game">
		    <li>      
		        [${game.id}] ${game.title}
		    </li>
		</c:forEach>
    <br><br>
</c:forEach>
</body>
</html>