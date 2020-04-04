<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>The game is ${game.title}!</h2>
<h5>Its ID is ${game.id}</h5>
<br><img height="500" src=${game.cover}></img><br>
${game.description}
</body>
</html>