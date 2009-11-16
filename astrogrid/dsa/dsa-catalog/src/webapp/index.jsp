<%@ page import="org.astrogrid.dataservice.service.DataServer,
                 org.astrogrid.dataservice.service.ServletHelper, 
                 org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = "."; // For the navigation include %>

<html>
<head>
<title><%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Welcome</h1>

<p>
Welcome to the web interface for this Astrogrid DSA/catalog installation. 
</p>
<p>
DSA/catalog is an AstroGrid service providing Virtual Observatory compliant 
access to data in an RDBMS database.
</p>

<h2>DSA/catalog installation: <%=DataServer.getDatacenterName() %></h2>
<p><%= ConfigFactory.getCommonConfig().getString("datacenter.description","(No description given)") %>
</p>

<h1>Interfaces</h1>
<p>
This DSA/catalog service provides the following IVOA interfaces:
</p>
<ul>
<li>
<a href='services/CommonExecutionConnectorService'>CEA</a> - an AstroGrid Common Execution Architecture interface for use by the AstroGrid Job Execution Service
</li>

<li>
<a href='SubmitCone'>ConeSearch</a> - An <a href='http://us-vo.org/pubs/files/conesearch.html'>NVO Cone Search</a>, a simple search on sky position.  <em>(This interface is optional, and can be disabled in your DSA/catalog configuration.)
</em>
</li>

<li>TAP - IVOA Table Access Protocol</li>

<!--
<li><a href='services/AxisDataService06'>'Native'</a> - SOAP interface providing most features, including queries using ADQL, a richly featured query language</td></li>
-->
</ul>


<h1>Installation Notes</h1>
<p>
  Please see the documentation index (via the top-left menu on this page) for installation
  instructions. If you are upgrading an existing DSA installation it is important to
  read the release notes: these will warn you if you need to adapt your configuration files.
</p>


</div>

</body>
</html>

