<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="card m-5 bg-very-light right-wave left-wave">
        <div class="card-header bg-very-dark text-white d-flex">
            <div>
                <h2 class="share-tech-mono">
                    <c:choose>
                        <c:when test="${! empty editMode && editMode == true}">
                            <spring:message code="navigation.editGame"/>
                        </c:when>
                        <c:otherwise>
                            <spring:message code="navigation.addGame"/>
                        </c:otherwise>
                    </c:choose>
                </h2>
            </div>
        </div>
        <div class="card-body d-flex flex-wrap justify-content-center padding-left-wave padding-right-wave">
            <form:form modelAttribute="gameForm" method="post" enctype="multipart/form-data" cssClass="w-100">
            	<form:input value="${gameId}" type="hidden" path="id"/>
                <div class="form-group">
                    <c:set var="title"><spring:message code="gameForm.title"/></c:set>
                    <form:label for="title" path="title"><strong>${title}</strong></form:label>
                    <form:errors path="title" class="form-error" element="p"/>
                    <form:errors class="form-error" element="p"/>
                    <form:input cssClass="form-control" path="title" name="title" type="text" placeholder="${title}"/>
                </div>
                <div class="form-group">
                    <c:set var="description"><spring:message code="gameForm.description"/></c:set>
                    <form:label for="description" path="description"><strong>${description}</strong></form:label>
                    <form:errors path="description" class="form-error" element="p"/>
                    <form:textarea cssClass="form-control" rows="8" path="description" name="description" type="text" placeholder="${description}"/>
                </div>
                <div class="form-group">
                    <form:label for="file" path="cover" ><strong><spring:message code="gameForm.cover"/></strong></form:label>
                    <form:errors path="cover" class="form-error" element="p"/>
                    <form:input path="cover" name="file" type="file" accept="image/png, image/jpeg" class="form-control-file"/>
                </div>
                <div class="form-group">
                    <c:set var="path" value="platforms"/>
                    <form:label path="${path}"><strong><spring:message code="gameForm.platforms"/></strong></form:label>

                    <c:set var="items" value="${allPlatforms}"/>
                    <%@include file="../common/listOfCheckableOptions.jsp"%>
                </div>
                <div class="form-group">
                    <c:set var="path" value="developers"/>
                    <form:label path="${path}"><strong><spring:message code="gameForm.developers"/></strong></form:label>

                    <c:set var="items" value="${allDevelopers}"/>
                    <%@include file="../common/listOfCheckableOptions.jsp"%>
                </div>
                <div class="form-group">
                    <c:set var="path" value="publishers"/>
                    <form:label path="${path}"><strong><spring:message code="gameForm.publishers"/></strong></form:label>

                    <c:set var="items" value="${allPublishers}"/>
                    <%@include file="../common/listOfCheckableOptions.jsp"%>
                </div>
                <div class="form-group">
                    <c:set var="path" value="genres"/>
                    <form:label path="${path}"><strong><spring:message code="gameForm.genres"/></strong></form:label>

                    <c:set var="items" value="${allGenres}"/>
                    <%@include file="../common/listOfCheckableOptions.jsp"%>
                </div>
                <div class="form-group">
                    <div>
                        <label><strong><spring:message code="gameForm.releaseDates"/></strong></label>
                    </div>
                    <c:forEach var="region" items="${allRegions}">
                        <label for="region-${region.shortName}">${region.name}</label>
                        <div class="input-group date mb-3" id="datetimepicker-${region.shortName}" data-target-input="nearest">
                            <c:choose>
                                <c:when test="${gameForm.releaseDates.containsKey(region.id)}">
                                    <input id="region-${region.shortName}" name="releaseDates['${region.id}']" type="text" class="form-control datetimepicker-input" data-target="#datetimepicker-${region.shortName}" value="${gameForm.releaseDates.get(region.id)}"/>
                                </c:when>
                                <c:otherwise>
                                    <input id="region-${region.shortName}" name="releaseDates['${region.id}']" type="text" class="form-control datetimepicker-input" data-target="#datetimepicker-${region.shortName}" />
                                </c:otherwise>
                            </c:choose>
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
                <c:choose>
                    <c:when test="${! empty editMode && editMode == true}">
                        <input type="submit" class="btn btn-primary btn-block mt-5" value="<spring:message code="gameForm.editSubmit"/>"/>
                    </c:when>
                    <c:otherwise>
                        <input type="submit" class="btn btn-primary btn-block mt-5" value="<spring:message code="gameForm.addSubmit"/>"/>
                    </c:otherwise>
                </c:choose>

            </form:form>
        </div>
    </div>
</body>
</html>
