<%@ page import="org.astrogrid.datacenter.service.DataServer, org.astrogrid.datacenter.service.ServletHelper, org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title><%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Welcome</h1>
<p>
Welcome to the PAL interface to <%=DataServer.getDatacenterName() %>.  PAL is the
Publisher's Astrogrid Library and gives Virtual Observatory compliant access to
this dataset.
</p>
<p> To query this data directly, you can use the 'Ask Query' on the left.
</p>
<h2><%=DataServer.getDatacenterName() %></h2>
<p><%= SimpleConfig.getSingleton().getString("datacenter.description","(No description given)") %>
</p>
<h1>Interfaces</h1>
<p>PAL provides a variety of IVO interfaces, including:
<p>
<table border='1' cellspacing='1' cellpadding='1'>
<tr><td>Type</td><td>Specification</td><td>Endpoint</td><td>Description</td></tr>
<tr><td>SkyNode</td><td>IVO</td><td><%= ServletHelper.getUrlStem() %>services/SkyNode074</td><td>IVO standard SOAP interface for querying using ADQL, a richly featured query language</td>
<tr><td>CEA</td><td>AstroGrid</td><td><%= ServletHelper.getUrlStem() %>services/CommonExecutionConnectorService</td><td>SOAP interface for use by the AstroGrid Job Execution Service</td>
<tr><td>Cone</td><td><a href='http://us-vo.org/pubs/files/conesearch.html'>NVO</a></td><td><%= ServletHelper.getUrlStem() %>SubmitCone</td><td>A simple search on sky position</td>
<tr><td>SIAP</td><td>IVO</a></td><td><%= ServletHelper.getUrlStem() %>SimpleImageAccess</td><td>Simple Image Access Protocol</td></tr>
<tr><td>SSAP</td><td>IVO</a></td><td><%= ServletHelper.getUrlStem() %>SimpleSpectraAccess</td><td>Simple Spectra Access Protocol</td></tr>
<tr><td>'Native'</td><td>AstroGrid</td><td><%= ServletHelper.getUrlStem() %>services/AxisDataService06</td><td>SOAP interface providing more features, using ADQL, a richly featured query language</td>
</table>
<p>Also there are these pages for direct interactive access, and various other servlets provide variations on the above.

<h1>Install Notes</h1>
<p>
If you have just installed PAL, then you can now run some
<a href="installChecks.jsp">checks</a> to run diagnostics, or
read how to <a href="docs/configure.jsp">configure</a> your datacenter.
</p>

</body>
</html>

