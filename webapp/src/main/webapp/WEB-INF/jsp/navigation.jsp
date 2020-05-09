<%--
    Include this page:
        <%@ include file="navigation.jsp"%>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<nav class="navbar navbar-expand-lg navbar-dark bg-primary sticky-top">
    <a class="navbar-brand" href="<c:url value="/"/>">
        <div class="navigation-logo">
            <h1><img src="<c:url value="/images/questlog-logo.png"/>" alt="<spring:message code="navigation.questLog"/>"/></h1>
        </div>
    </a>
    <div class="w-100" id="navbarSupportedContent">
        <ul class="navbar-nav d-flex w-100">
            <li class="nav-item flex-grow-1 ml-lg-3">
                <div class="nav-link w-100">
                    <%@include file="navigationSearchBar.jsp"%>
                </div>
            </li>
            <li class="nav-item">
                <div class="nav-link">
                    <a class="btn btn-dark w-100" role="button" href="<c:url value="/explore"/>"><spring:message code="navigation.explore"/></a>
                </div>
            </li>
            <c:choose>
                <c:when test="${loggedUser == null}">
                    <li class="nav-item mt-3 mt-lg-0 ml-lg-3">
                        <div class="nav-link">
                            <a class="btn btn-outline-secondary w-100" role="button" href="<c:url value="/login"/>"><spring:message code="navigation.login"/></a>
                        </div>
                    </li>
                    <li class="nav-item">
                        <div class="nav-link">
                            <a class="btn btn-outline-secondary w-100" role="button" href="<c:url value="/create"/>"><spring:message code="navigation.signup"/></a>
                        </div>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="nav-item d-flex flex-column justify-content-center mt-3 mt-lg-0 ml-lg-3">
                        <div class="nav-link">
                            <p class="m-0 text-white w-100 text-center"><c:out value="${loggedUser.username}"/></p>
                        </div>
                    </li>
                    <li class="nav-item">
                        <div class="nav-link">
                            <a class="btn btn-outline-secondary w-100" role="button" href="<c:url value="/logout"/>"><spring:message code="navigation.logout"/></a>
                        </div>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>
</nav>