<?xml version="1.0" encoding="UTF-8"?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<uws:jobList xmlns:uws="http://www.ivoa.net/xml/UWS/v0.9.2" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.ivoa.net/xml/UWS/v0.9.2 ../../../../astrogrid-contracts/src/schema/cea/UWS/v0.9/UWS.xsd ">
<c:set var="base" value="${requestContext.requestUri}"></c:set>
<c:forEach items="${jobs}" var="theJob">
  <uws:jobref id="${theJob.jobId}" xlink:href="${base}/${theJob.jobId}/">
    <uws:phase>${theJob.phase}</uws:phase>
  </uws:jobref>
</c:forEach>
</uws:jobList>
