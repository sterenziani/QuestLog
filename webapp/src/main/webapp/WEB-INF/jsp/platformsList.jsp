<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h2>LIST OF PLATFORMS</h2>
<br><br>
<c:forEach items="${platforms}" var="platform">
        <h2>[${platform.id} / ${platform.shortName}] ${platform.name}</h2>
        <br><img height="70" src=${platform.logo}></img>
		<c:forEach items="${platform.games}" var="game">
		    <li>      
		        [${game.id}] ${game.title}
		    </li>
		    <br>
		</c:forEach>
    <br><br>
</c:forEach>
</body>
</html>