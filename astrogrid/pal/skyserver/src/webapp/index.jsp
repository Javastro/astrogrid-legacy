<%@ page import="org.astrogrid.dataservice.service.DataServer,
                 org.astrogrid.dataservice.service.ServletHelper, org.astrogrid.config.SimpleConfig"
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
<%--
<table border='1' cellspacing='0' cellpadding='1'>
<tr><td>Type/Endpoint</td><td>Specification</td><td>Description</td></tr>
<tr><td><a href='<%= ServletHelper.getUrlStem() %>services/SkyNode074'>SkyNode</a></td><td>IVO</td>      <td>IVO standard SOAP interface for querying using ADQL, a richly featured query language</td>
<tr><td><a href='<%= ServletHelper.getUrlStem() %>services/CommonExecutionConnectorService'>CEA</a></td> <td>AstroGrid</td><td>SOAP interface for use by the AstroGrid Job Execution Service</td>
<tr><td><a href='<%= ServletHelper.getUrlStem() %>SubmitCone'>Cone</a></td>                              <td><a href='http://us-vo.org/pubs/files/conesearch.html'>NVO</a></td><td>A simple search on sky position</td>
<tr><td><a href='<%= ServletHelper.getUrlStem() %>SimpleImageAccess'>SIAP</a></td>                       <td>IVO</a></td><td>Simple Image Access Protocol</td></tr>
<tr><td><a href='<%= ServletHelper.getUrlStem() %>SimpleSpectraAccess'>SSAP</a></td>                     <td>IVO</a></td><td>Simple Spectra Access Protocol</td></tr>
<tr><td><a href='<%= ServletHelper.getUrlStem() %>services/AxisDataService06'>'Native'</a></td>          <td>AstroGrid</td><td>SOAP interface providing more features, using ADQL, a richly featured query language</td>
</table>
--%>
<ul>
<li><a href='services/CommonExecutionConnectorService'>CEA</a> - >SOAP interface for use by the AstroGrid Job Execution Service
<li><a href='SubmitCone'>Cone</a> - An <a href='http://us-vo.org/pubs/files/conesearch.html'>NVO Cone Search</a>, a simple search on sky position
<li><a href='SimpleImageAccess'>SIAP</a> - Simple Image Access Protocol</td></tr>
<li><a href='SimpleSpectraAccess'>SSAP</a> - Simple Spectra Access Protocol</td></tr>
<li><a href='services/AxisDataService06'>'Native'</a> - SOAP interface providing most features, including queries using ADQL, a richly featured query language</td>
<li><a href='services/SkyNode074'>SkyNode</a> (<i><b>Not Working</i></b>) - IVO standard SOAP interface</td>
</ul>

<p>Also there are these pages for direct interactive access, and various other servlets provide variations on the above.

<h1>Install Notes</h1>
<p>
If you have just installed PAL, then you can now run some
<a href="installChecks.jsp">checks</a> to run diagnostics, or
read how to <a href="docs/configure.jsp">configure</a> your datacenter.
</p>

</body>
</html>

