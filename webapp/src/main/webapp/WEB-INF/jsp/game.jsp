<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>The game is ${game.title}!</h2>
<c:forEach items="${game.genres}" var="genre">   
        ---      ${genre.name}
</c:forEach>
<br>
<h5>Its ID is ${game.id}</h5>
<h5>This game is available for:</h5>
<c:forEach items="${game.platforms}" var="platform">
    <li>      
        [${platform.id}] ${platform.name}
    </li>
    <br>
</c:forEach>
<br><img height="500" src=${game.cover}></img><br>
${game.description}
</body>
</html>