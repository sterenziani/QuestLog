<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1>LIST OF DEVELOPERS</h1>
<br><br>
<c:forEach items="${developers}" var="developer">
    <li>      
        [${developer.id}] ${developer.name}
        <br><img height="70" src=${developer.logo}></img>
    </li>
    <br>
</c:forEach>
</body>
</html>