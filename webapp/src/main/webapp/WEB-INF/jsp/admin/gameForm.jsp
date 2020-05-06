<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../commonHead.jsp"%>
</head>
<body>
    <%@include file="../navigation.jsp"%>
    <c:url value="/admin/game" var="postPath"/>
    <div class="content">
        <form:form modelAttribute="gameForm" action="${postPath}" method="post" enctype="multipart/form-data">
            <div>
                <label>
                    <form:input path="image" name="file" type="file"/>
                </label>
            </div>
            <div class="form-field">
                <input type="submit" class="button" value="Upload"/>
            </div>
        </form:form>
    </div>
</body>
</html>
