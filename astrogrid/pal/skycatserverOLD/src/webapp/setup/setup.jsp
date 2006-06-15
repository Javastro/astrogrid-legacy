<%@ page import="org.astrogrid.dataservice.service.DataServer, org.astrogrid.webapp.SetProperties"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>Publisher's Astrogrid Library Setup for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Setup Configuration</h1>
<p>
These are the 'wizard' setup pages for initial configuration of a PAL site, giving
a point & click interface to setting the configuration options of your site.
</p>
<h2>Contexts</h2>
<p>(erm, wwhat about new context?)
<h2>General Settings</h2>
<p>
<form action='SetProperties' method='post'>

  <!-- parameter to tell the setProperties.jsp to return to this form when done -->
  <input type='hidden' name='returnTo' value='setup.jsp'/>

  <table>
  <tr>
   <%= SetProperties.makePropertyInput("datacenter.name", "General brief name, or title, of this PAL installation", null) %>
  </tr>
  <tr>
   <%= SetProperties.makePropertyInput("org.astrogrid.registry.admin.endpoint", "Default Registry endpoint", "http://hydra.star.le.ac.uk:8080/astrogrid-registry/services/Admin") %>
  </tr>
  </table>

</form>


<h2>Standard Setups</h2>
<p>
These pages help you set up a PAL site for the following 'standard' configurations:
<ul>
<li><a href='rdbms/index.jsp'>Relational Database Management System</a> (eg SQL Server, MySQL)
<li>FITS file collection
<li>Warehouse
</ul>

</body>
</html>

