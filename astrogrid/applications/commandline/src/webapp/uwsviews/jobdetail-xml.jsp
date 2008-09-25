<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!-- this page generates large numbers of line feeds between the xml for some reason... -->
<uws:job xmlns:uws="http://www.ivoa.net/xml/UWS/v0.9.2" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.ivoa.net/xml/UWS/v0.9.2 ../../../../astrogrid-contracts/src/schema/cea/UWS/v0.9/UWS.xsd ">
<c:set var="base" value="${requestContext.requestUri}"></c:set>
  <uws:jobId>${JobId}</uws:jobId>
  <uws:phase>${theJob.phase}</uws:phase>
  <c:choose>
  <c:when test="${empty theJob.startTime}" >
    <uws:startTime xsi:nil="true"></uws:startTime>
  </c:when>
  <c:otherwise>
    <uws:startTime>${theJob.startTime}</uws:startTime>
  </c:otherwise>
  </c:choose>
  <c:choose>
  <c:when test="${empty theJob.endTime}" >
    <uws:endTime xsi:nil="true"></uws:endTime>
  </c:when>
  <c:otherwise>
    <uws:endTime>${theJob.endTime}</uws:endTime>
  </c:otherwise>
  </c:choose>
  <c:choose>
  <c:when test="${empty theJob.executionDuration}" >
    <uws:executionDuration xsi:nil="true"></uws:executionDuration>
  </c:when>
  <c:otherwise>
    <uws:executionDuration>${theJob.executionDuration}</uws:executionDuration>
  </c:otherwise>
  </c:choose>
  <c:choose>
  <c:when test="${empty theJob.destruction}" >
    <uws:destruction xsi:nil="true"></uws:destruction>
  </c:when>
  <c:otherwise>
    <uws:destruction>${theJob.destruction}</uws:destruction>
  </c:otherwise>
  </c:choose>
  <c:if test='${theJob.phase eq "ERROR"}'>
  <uws:errorSummary>
    <uws:message>${theJob.errormessage}</uws:message>
    <uws:detail href="${base}/error"/>
  </uws:errorSummary>
  </c:if>
</uws:job>

