<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>The genre is ${genre.name}!</h2>
<h5>Its ID is ${genre.id}</h5>
<c:forEach items="${genre.games}" var="game">
    <li>      
        [${game.id}] ${game.title}
    </li>
    <br>
</c:forEach>
<br><img height="100" width="100" src=${genre.logo}></img><br>
</body>
</html>