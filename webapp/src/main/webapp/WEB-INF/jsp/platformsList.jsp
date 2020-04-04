<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<h1>LIST OF PLATFORMS</h1>
<br><br>
<c:forEach items="${platforms}" var="platform">
    <li>      
        [${platform.id} / ${platform.shortName}] ${platform.name}
        <br><img height="70" src=${platform.logo}></img>
    </li>
    <br>
</c:forEach>
</body>
</html>