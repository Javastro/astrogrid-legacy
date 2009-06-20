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
<% String server = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort(); %>

<div id="bodyColumn">
  <div class="contentBox">

<div class="section" >
<h2>Resources for this CEC</h2>
<p>The links pointed to by this page will produce xml documents suitable for inclusion in an <a href="http://www.ivoa.net/Documents/latest/RegistryInterface.html">IVOA registry</a></p>
<p>If the registry has the capability of registering from the content of a URL then you can use the URLs listed below to register each component - alternatively just save the content to a file and upload or copy and paste.</p>

<ul>
<li><a href="${base}/all">All resources in single document</a></li>
<li><a href="${base}/server">Server only</a> <b><c:out value="${server.id}"/></b> <c:if test="${server.registered}"> <em>(already registered)</em></c:if> </li>
</ul>
<h4>Individual Applications</h4>
<ul>
  <c:forEach items="${apps}" var="theApp">
    <li><a href="${base}/app/${theApp.shortref}/res"><c:out value="${theApp.name}"/></a> <b><c:out value="${theApp.id}"/></b>
    <c:if test="${theApp.registered}"> <em>(already registered)</em></c:if>
    </li>
  </c:forEach>

</ul>
<h3>Registry</h3>
<p>The registry (<c:out value="${registry.name}"></c:out>, <c:out value="${registry.id}"></c:out>) that you are using to query from is at <a href="${registry.endpoint}"><c:out value="${registry.endpoint}"></c:out></a> - you might also be able to use this as a publishing registry to register this service.
However, note that the only authority Ids that this registry will allow you to publish under are; </p>
<ul>
  <c:forEach items="${registry.managedAuthorities}" var="authority">
  <li><c:out value="${authority}"/></li>
  </c:forEach>
</ul>
<c:if test="${registry.astrogridRegistry}">
<h4>Astrogrid Registry</h4>

<p>It appears that you are using an AstroGrid registry - in this case it might be possible to register using these buttons if you have appropriate privileges (and the registry is a pubishing registry for the appropriate authority ids).</p>

<ul>

<li><form method="post" action="${registry.endpoint}/../admin/addResourceEntry_backdoor.jsp">
<input type="hidden" name="docurl" value="<%=server %>${base}/server" /> 
<input type="hidden" name="addFromURL" value="true" />
 Server <c:out value="${server.id}"/>  <input type="submit" name="uploadFromURL" value="upload" />
</form>
<c:if test="${server.registered}">
        <form action="${registry.endpoint}/../registration/ServiceMetadata" method="post">
	      <input type="hidden" name="IVORN" value="${server.id}"/>
          <input type="hidden" name="VOSI_Capabilities" value="<%=server + request.getContextPath() %>/VOSI/capabilities" />
          <input type="submit" value="Update the registry entry using VOSI"/>
        </form>        
</c:if>
</li>
 <c:forEach items="${apps}" var="theApp">
 <li>
 <form method="post" action="${registry.endpoint}/../admin/addResourceEntry_backdoor.jsp">
<input type="hidden" name="docurl" value="<%=server %>${base}/app/${theApp.shortref}/res" />
<input type="hidden" name="addFromURL" value="true" />
 Application <c:out value="${theApp.id}"/>  <input type="submit" name="uploadFromURL" value="upload" />
</form>
</li>
</c:forEach>
</ul>
</c:if>

</div>
</div>
</div>
<%@ include file="../inc/footer.jsp" %>
