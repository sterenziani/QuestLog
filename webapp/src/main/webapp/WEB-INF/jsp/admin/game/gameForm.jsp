<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../../commonHead.jsp"%>
</head>
<body>
    <%@include file="../../navigation.jsp"%>
    <div class="card m-5 bg-very-light right-wave left-wave">
        <div class="card-header bg-very-dark text-white d-flex">
            <div>
                <h2 class="share-tech-mono"><spring:message code="navigation.addGame"/></h2>
            </div>
        </div>
        <div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
            <c:url value="/admin/game/new" var="postPath"/>
            <div class="content">
                <form:form modelAttribute="gameForm" action="${postPath}" method="post" enctype="multipart/form-data">
                    <div class="form background-color">
                        <div class="form-element-column">
                            <c:set var="title"><spring:message code="gameForm.title"/></c:set>
                            <div class="form-label">
                                <form:label for="title" path="title">${title}</form:label>
                            </div>
                            <div class="form-field">
                                <form:errors path="title" class="form-error" element="p"/>
                                <form:input cssClass="full-width" path="title" name="title" type="text" placeholder="${title}"/>
                            </div>
                        </div>
                        <div class="form-element-column">
                            <c:set var="description"><spring:message code="gameForm.description"/></c:set>
                            <div class="form-label">
                                <form:label for="description" path="description">${description}</form:label>
                            </div>
                            <div class="form-field">
                                <form:errors path="description" class="form-error" element="p"/>
                                <form:textarea cssClass="full-width vertical-resize" rows="8" path="description" name="description" type="text" placeholder="${description}"/>
                            </div>
                        </div>
                        <div class="form-element-column">
                            <div class="form-label">
                                <form:label for="file" path="cover"><spring:message code="gameForm.cover"/></form:label>
                            </div>
                            <div class="form-field">
                                <form:errors path="cover" class="form-error" element="p"/>
                                <form:input path="cover" name="file" type="file" accept="image/png, image/jpeg"/>
                            </div>
                        </div>
                        <div class="form-element-row">
                            <div class="form-label">
                                <form:label path="${path}"><spring:message code="gameForm.platforms"/></form:label>
                            </div>
                            <div class="form-field">
                                <c:set var="path" value="platforms"/>
                                <c:set var="items" value="${platforms}"/>
                                <%@include file="../../common/listOfCheckableOptions.jsp"%>
                            </div>
                        </div>
                        <div class="form-element-row">
                            <div class="form-label">
                                <form:label path="${path}"><spring:message code="gameForm.developers"/></form:label>
                            </div>
                            <div class="form-field">
                                <c:set var="path" value="developers"/>
                                <c:set var="items" value="${developers}"/>
                                <%@include file="../../common/listOfCheckableOptions.jsp"%>
                            </div>
                        </div>
                        <div class="form-element-row">
                            <div class="form-label">
                                <form:label path="${path}"><spring:message code="gameForm.publishers"/></form:label>
                            </div>
                            <div class="form-field">
                                <c:set var="path" value="publishers"/>
                                <c:set var="items" value="${publishers}"/>
                                <%@include file="../../common/listOfCheckableOptions.jsp"%>
                            </div>
                        </div>
                        <div class="form-element-row">
                            <div class="form-label">
                                <form:label path="${path}"><spring:message code="gameForm.genres"/></form:label>
                            </div>
                            <div class="form-field">
                                <c:set var="path" value="genres"/>
                                <c:set var="items" value="${genres}"/>
                                <%@include file="../../common/listOfCheckableOptions.jsp"%>
                            </div>
                        </div>
                        <div>
                            <c:forEach var="region" items="${regions}">
                                <label for="region-${region.shortName}">${region.name}</label>
                                <div class="input-group date" id="datetimepicker-${region.shortName}" data-target-input="nearest">
                                    <input id="region-${region.shortName}" name="releaseDates['${region.id}']" type="text" class="form-control datetimepicker-input" data-target="#datetimepicker-${region.shortName}"/>
                                    <div class="input-group-append" data-target="#datetimepicker-${region.shortName}" data-toggle="datetimepicker">
                                        <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                                    </div>
                                </div>
                                <script type="text/javascript">
                                    $(function () {
                                        $('#datetimepicker-${region.shortName}').datetimepicker({
                                            format: 'YYYY-MM-DD'
                                        });
                                    });
                                </script>
                            </c:forEach>
                        </div>
                        <div class="form-element-row">
                            <input type="submit" class="button" value="Upload"/>
                        </div>
                    </div>
                </form:form>
            </div>
        </div>
    </div>
</body>
</html>
