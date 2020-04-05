<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1>LIST OF PUBLISHERS</h1>
<br><br>
<c:forEach items="${publishers}" var="publisher">
    <li>      
        [${publisher.id}] ${publisher.name}
        <br><img height="70" src=${publisher.logo}></img>
    </li>
    <br>
</c:forEach>
</body>
</html>