<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/xml" prefix="x"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="org.astrogrid.applications.contracts.CEAConfiguration"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Update Config</title>
</head>
<body>
<h1>Update Config file</h1>
<p>This page will update the config file from 2007.x version to 2008.x </p>
<% 
ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(pageContext.getServletContext()); 
CEAConfiguration conf = (CEAConfiguration)ctx.getBean("config") ;  
%>
<form method="get" action="updateConfig.jsp">
<table width="100%">
	<tr>
		<td>
		<p>original config file location</p>
		</td>
		<td><input name="cfile" value="<%= conf.getApplicationDescriptionUrl() %>" size="60" /></td>
	</tr>
	<tr>
		<td><p>original registry template location</p></td>
		<td><input name="regfile" value="<%= conf.getRegistryTemplate() %>" size="60"/></td>
	</tr>
	<tr>
	  <td><input type="submit"  value="update"/></td><td>To make transformed document appear below</td>
	</tr>
	<tr>
	  <td colspan="2">
	  <textarea cols="80" rows="40">
	  <c:if test="${not empty param['cfile']}">
      <c:import var="xml" url="${param['cfile']}"/>
      <c:import var="xslt" url="../config/CEA_UpdateConfigTo1.0.xsl"/>
      <x:transform xml="${xml}" xslt="${xslt}">
        <x:param name="regtemplate" value="${param['regfile']}"></x:param>
      </x:transform>
   
   </c:if>
	  </textarea></td>
	</tr>
</table>

</form>

</body>
</html>