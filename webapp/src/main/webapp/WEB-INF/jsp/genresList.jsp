<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1>LIST OF GENRES</h1>
<br><br>
<c:forEach items="${genres}" var="genre">
    <li>      
        [${genre.id}] ${genre.name}
        <br><img height="100" width="100" src=${genre.logo}></img>
    </li>
    <br>
</c:forEach>
</body>
</html>