<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1>LIST OF GAMES</h1>
<br><br>
<c:forEach items="${games}" var="game">
    <li>      
        <h2>[${game.id}] ${game.title}</h2>
		<br><img height="200" src=${game.cover}></img><br>
		${game.description}
    </li>
    <br>
</c:forEach>
</body>
</html>