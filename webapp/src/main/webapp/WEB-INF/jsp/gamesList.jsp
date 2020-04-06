<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1>LIST OF GAMES</h1>
<br><br>
<c:forEach items="${games}" var="game">    
        <h2>[${game.id}] ${game.title}</h2>
		<br><img height="200" src=${game.cover}></img><br>
		<c:forEach items="${game.genres}" var="genre">   
		        ---      ${genre.name}
		</c:forEach>
		<br>
		<c:forEach items="${game.releaseDates}" var="release">   
		        <li>${release.region.name}: ${release.date}</li>
		</c:forEach>
		<br>
		<h5>This game is available for:</h5>
		<c:forEach items="${game.platforms}" var="platform">
		    <li>      
		        [${platform.id}] ${platform.name}
		    </li>
		</c:forEach>
		<h5>This game was developed by:</h5>
		<c:forEach items="${game.developers}" var="developer">
		    <li>      
		        [${developer.id}] ${developer.name}
		    </li>
		</c:forEach>
		<h5>This game was published by:</h5>
		<c:forEach items="${game.publishers}" var="publisher">
		    <li>      
		        [${publisher.id}] ${publisher.name}
		    </li>
		</c:forEach>
    <br><br><br>
</c:forEach>
</body>
</html>