<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>The publisher is ${publisher.name}!</h2>
<h5>Its ID is ${publisher.id}</h5>
<br><img height="100" src=${publisher.logo}></img><br>
<br>
<h4>Published games:</h4>
<c:forEach items="${publisher.games}" var="game">
    <li>      
        [${game.id}] ${game.title}
    </li>
    <br>
</c:forEach>
</body>
</html>