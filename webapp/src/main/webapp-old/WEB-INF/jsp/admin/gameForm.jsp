<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
     <c:choose>
         <c:when test="${! empty editMode && editMode == true}">
            <spring:message code="navigation.editGame" var="title"/>
             <title>QuestLog - <c:out value="${title}"/></title>
         </c:when>
         <c:otherwise>
             <spring:message code="navigation.addGame" var="title"/>
             <title>QuestLog - <c:out value="${title}"/></title>
         </c:otherwise>
     </c:choose>
</head>
<body>
    <%@include file="../common/navigation.jsp"%>
    <div class="card m-5 bg-very-light right-wave left-wave">
        <div class="card-header bg-very-dark text-white d-flex">
            <div>
                <h2 class="share-tech-mono">
                    <c:choose>
                        <c:when test="${! empty editMode && editMode == true}">
                            <spring:message code="navigation.editGame" var="editGame"/>
                            <c:out value="${editGame}"/>
                        </c:when>
                        <c:otherwise>
                            <spring:message code="navigation.addGame" var="addGame"/>
                            <c:out value="${addGame}"/>
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
                    <form:label for="title" path="title"><strong><c:out value="${title}"/></strong></form:label>
                    <form:errors path="title" class="form-error" element="p"/>
                    <form:errors class="form-error" element="p"/>
                    <form:input cssClass="form-control" path="title" name="title" type="text" placeholder="${title}"/>
                </div>
                <div class="form-group">
                    <c:set var="description"><spring:message code="gameForm.description"/></c:set>
                    <form:label for="description" path="description"><strong><c:out value="${description}"/></strong></form:label>
                    <form:errors path="description" class="form-error" element="p"/>
                    <form:textarea cssClass="form-control" rows="8" path="description" name="description" type="text" placeholder="${description}"/>
                </div>
                <div class="form-group">
                    <form:label for="file" path="cover" ><strong><spring:message code="gameForm.cover"/></strong></form:label>
                    <form:errors path="cover" class="form-error" element="p"/>
                    <form:input path="cover" name="file" type="file" accept="image/png, image/jpeg" class="form-control-file"/>
                </div>
                <div class="form-group">
                    <form:label for="trailer" path="trailer" ><strong><spring:message code="gameForm.trailer"/></strong></form:label>
                    <form:errors path="trailer" class="form-error" element="p"/>
                    <c:set var="trailer"><spring:message code="gameForm.trailerPlaceholder"/></c:set>
                    <form:input onchange="updatePreview(this.value)" cssClass="form-control" path="trailer" name="trailer" type="text" placeholder="${trailer}"/>

                </div>
                <div class="form-group">
                    <div class="mb-2">
                        <spring:message code="gameForm.trailer.preview" var="preview"/>
                        <strong><c:out value="${preview}"/></strong>
                    </div>
                    <iframe id="preview" width="286" height="161" src="https://www.youtube.com/embed/<c:out value="${gameForm.trailer}"/>" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                    <script>

                        function updatePreview(val){
                            document.getElementById("preview").src = "https://www.youtube.com/embed/" + val;
                        }
                    </script>
                </div>

                <div class="form-group">
                    <c:set var="path" value="platforms"/>
                    <spring:message code="gameForm.platforms" var="platforms"/>
                    <form:label path="${path}"><strong><c:out value="${platforms}"/></strong></form:label>

                    <c:set var="items" value="${allPlatforms}"/>
                    <%@include file="../common/listOfCheckableOptions.jsp"%>
                </div>
                <div class="form-group">
                    <c:set var="path" value="developers"/>
                    <spring:message code="gameForm.developers" var="devs"/>
                    <form:label path="${path}"><strong><c:out value="${devs}"/></strong></form:label>

                    <c:set var="items" value="${allDevelopers}"/>
                    <%@include file="../common/listOfCheckableOptions.jsp"%>
                </div>
                <div class="form-group">
                    <c:set var="path" value="publishers"/>
                    <spring:message code="gameForm.publishers" var="pubs"/>
                    <form:label path="${path}"><strong><c:out value="${pubs}"/></strong></form:label>

                    <c:set var="items" value="${allPublishers}"/>
                    <%@include file="../common/listOfCheckableOptions.jsp"%>
                </div>
                <div class="form-group">
                    <c:set var="path" value="genres"/>
                    <spring:message code="gameForm.genres" var="gens"/>
                    <form:label path="${path}"><strong><c:out value="${gens}"/></strong></form:label>

                    <c:set var="items" value="${allGenres}"/>
                    <%@include file="listOfCheckableGenreOptions.jsp"%>
                </div>
                <div class="form-group">
                    <div>
                        <label><strong><spring:message code="gameForm.releaseDates"/></strong></label>
                    </div>
                    <c:forEach var="region" items="${allRegions}">
                        <label for="region-<c:out value="${region.shortName}"/>"><c:out value="${region.shortName}"/></label>
                        <div class="input-group date mb-3" id="datetimepicker-<c:out value="${region.shortName}"/>" data-target-input="nearest">
                            <c:choose>
                                <c:when test="${gameForm.releaseDates.containsKey(region.id)}">
                                    <input id="region-<c:out value="${region.shortName}"/>" name="releaseDates['<c:out value="${region.id}"/>']" type="text" class="form-control datetimepicker-input" data-target="#datetimepicker-<c:out value="${region.shortName}"/>" value="<c:out value="${gameForm.releaseDates.get(region.id)}"/>"/>
                                </c:when>
                                <c:otherwise>
                                    <input id="region-<c:out value="${region.shortName}"/>" name="releaseDates['<c:out value="${region.id}"/>']" type="text" class="form-control datetimepicker-input" data-target="#datetimepicker-<c:out value="${region.shortName}"/>" />
                                </c:otherwise>
                            </c:choose>
                            <div class="input-group-append" data-target="#datetimepicker-<c:out value="${region.shortName}"/>" data-toggle="datetimepicker">
                                <div class="input-group-text"><i class="fa fa-calendar"></i></div>
                            </div>
                        </div>
                        <script type="text/javascript">
                            $(function () {
                                $('#datetimepicker-<c:out value="${region.shortName}"/>').datetimepicker({
                                    format: 'YYYY-MM-DD'
                                });
                            });
                        </script>
                    </c:forEach>
                </div>
                <c:choose>
                    <c:when test="${! empty editMode && editMode == true}">
                        <spring:message code="gameForm.editSubmit" var="editSubmit"/>
                        <input type="submit" class="btn btn-primary btn-block mt-5" value="<c:out value="${editSubmit}"/>"/>
                    </c:when>
                    <c:otherwise>
                        <spring:message code="gameForm.addSubmit" var="addSubmit"/>
                        <input type="submit" class="btn btn-primary btn-block mt-5" value="<c:out value="${addSubmit}"/>"/>
                    </c:otherwise>
                </c:choose>
            </form:form>
        </div>
    </div>
</body>
</html>
