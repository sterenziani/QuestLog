<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>The platform [${platform.shortName}] is ${platform.name}!</h2>
<h5>Its ID is ${platform.id}</h5>
<br><img height="70" src=${platform.logo}></img>
<c:forEach items="${platform.games}" var="game">
    <li>      
        [${game.id}] ${game.title}
    </li>
    <br>
</c:forEach>
</body>
</html>