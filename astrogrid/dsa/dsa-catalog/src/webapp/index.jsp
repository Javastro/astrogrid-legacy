<%@ page import="org.astrogrid.dataservice.service.DataServer,
                 org.astrogrid.dataservice.service.ServletHelper, 
                 org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
   contentType="text/html"
   pageEncoding="UTF-8"
%>
<% String pathPrefix = "."; // For the navigation include %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title><%=DataServer.getDatacenterName() %></title>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
<style type="text/css" media="all">@import url("./style/astrogrid.css");</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Welcome</h1>
<p>
DSA/catalogue is an AstroGrid service providing IVOA-compliant
access to data in a relational database.
</p>

<h2>DSA/catalog installation: <%=DataServer.getDatacenterName() %></h2>
<p><%= ConfigFactory.getCommonConfig().getString("datacenter.description","(No description given)") %>
</p>

<h2>Interfaces</h2>
<p>
This DSA/catalog service provides the following interfaces:
</p>
<ul>
<li>
CEC - an AstroGrid Common Execution Architecture interface for use by the AstroGrid Job Execution Service
</li>

<li>
Cone Search - IVOA Simple Cone-Search, a search on sky position.
(This interface is optional, and can be disabled in your DSA/catalog configuration.)
</li>

<li>TAP - IVOA Table Access Protocol</li>

</ul>


<h2>Installation Notes</h2>
<p>
  Please see the documentation index (via the top-left menu on this page) for installation
  instructions. If you are upgrading an existing DSA installation it is important also to
  read the release notes: these will warn you if you need to adapt your configuration files.
</p>


</div>

</body>
</html>

