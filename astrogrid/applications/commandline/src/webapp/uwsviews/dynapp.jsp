<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"
%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>


<%@page import="org.astrogrid.applications.contracts.Configuration"%>
<%@page import="java.net.URL"%><html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Dynamically Defined Applications</title>
<jsp:include page="../inc/header.jsp" flush="true" />
<!-- body -->
<div id="bodyColumn">

<div class="contentBox">
<c:set var="base" value="${requestContext.requestUri}"></c:set>

<h1>Dynamically Defined Application</h1>
<table>
<tr><td>id</td><td><c:out value="${app.id}"/></td></tr>
<tr><td>name</td><td><c:out value="${app.name}"/></td></tr>
<tr><td>description</td><td><c:out value="${app.description}"/></td></tr>
</table>
<%
//naughty direct use of the application context - lazy should go into mvc
ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext());
URL url =  ((Configuration)ctx.getBean("config")).getApplicationDescriptionUrl();
%>    

<form action="/uws/appDefine/save" method="get">
save in <input name="location" type="text" value="<%=url%>${app.name}.xml" size="60"/>
<input type="submit" value="write" />
</form>
</div>
</div>
<jsp:include page="../inc/footer.jsp" flush="true" />