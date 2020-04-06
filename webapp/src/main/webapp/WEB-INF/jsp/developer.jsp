<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>The developer is ${developer.name}!</h2>
<h5>Its ID is ${developer.id}</h5>
<br><img height="100" src=${developer.logo}></img><br>
<br>
<h4>Developed games:</h4>
<c:forEach items="${developer.games}" var="game">
    <li>      
        [${game.id}] ${game.title}
    </li>
    <br>
</c:forEach>
</body>
</html>