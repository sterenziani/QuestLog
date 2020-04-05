<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1>LIST OF GENRES</h1>
<br><br>
<c:forEach items="${genres}" var="genre">
    <li>      
        [${genre.id}] ${genre.name}
    </li>
    <br>
</c:forEach>
</body>
</html>