<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../common/commonHead.jsp"%>
    <title>QuestLog - <spring:message code="navigation.explore"/></title>
</head>
<body class="background-primary">
    <%@include file="../common/navigation.jsp"%>
    <div>
        <div>
	        <c:if test="${genresCropped}">
				<c:set var="seeAllGenresUrl" value="/genres"/>
			</c:if>
	        <%@ include file="genresList.jsp"%>
    	</div>
        <div>
	        <c:if test="${platformsCropped}">
				<c:set var="seeAllPlatformsUrl" value="/platforms"/>
			</c:if>
	        <%@ include file="platformsList.jsp"%>
    	</div>
    	<div>
    		<c:if test="${developersCropped}">
				<c:set var="seeAllDevsUrl" value="/developers"/>
			</c:if>
	        <%@ include file="developersList.jsp"%>
    	</div>
    	<div>
    		<c:if test="${publishersCropped}">
				<c:set var="seeAllPubsUrl" value="/publishers"/>
			</c:if>
	        <%@ include file="publishersList.jsp"%>
    	</div>
    </div>
</body>
</html>