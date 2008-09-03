<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>CEA Resource List</title>
<%@ include file="../inc/header.jsp" %>
<c:set var="base" value="${requestContext.requestUri}"></c:set>
<div id="bodyColumn">
  <div class="contentBox">

<div class="section" >
<h2>Resources for this CEC</h2>
<p>The links pointed to by this page will produce xml documents suitable for inclusion in an IVOA registry</p>
<p>If the registry has the capability of registering from the content of a URL then you can use the URLs listed below to register each component</p>
<ul>
<li><a href="${base}/all">All resources in single document</a></li>
<li><a href="${base}/server">Server only</a></li>
</ul>
<h3>Individual Applications</h3>
<ul>
  <c:forEach items="${apps}" var="theApp">
    <li><a href="${base}/app/${theApp.shortref}/res"><c:out value="${theApp.name}"/></a> <c:out value="${theApp.id}"/></li>
  </c:forEach>

</ul>
</div>
</div>
</div>
<%@ include file="../inc/footer.jsp" %>
