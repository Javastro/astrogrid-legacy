<%@ page import="java.io.*,
         org.astrogrid.cfg.ConfigFactory,
         org.astrogrid.dataservice.service.DataServer"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = "."; // For the navigation include %>

<html>
<head>
<title><%=DataServer.getDatacenterName() %> Astrogrid DSA/catalog installation</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>
<h1>Testing your configuration</h1>

<p>
The DSA/catalog service provides a number of pages for self-testing, to
be found in the 'Self-tests' menu in the main menubar on the left of
this page.
</p>

<p>
<strong>All the self-tests</strong> need to pass;  if any of them are
failing, you need to revisit your <a href="configure.jsp">configuration</a>
and check for errors.
</p>

<p>
In summary, the self-test functions are as follows:
</p>

<ul>
    <li><a href="admin/happyaxis.jsp">Apache Axis self-check</a>
    for validating the AXIS installation's configuration.</a><br/>
    DSA/catalog uses Apache Axis as its underlying SOAP transport mechanism. 
    It is not necessarily problematic if "optional components" are missing.
    If this validation page displays an exception instead of a status 
    page, the likely cause is that you have multiple XML parsers in your 
    classpath.
    </li>
   <li> <a href="admin/TestServlet?suite=org.astrogrid.dataservice.service.InstallationSelfCheck">Installation self-test</a> for checking basic service configuration,</li> 
    <li><a href="admin/TestServlet?suite=org.astrogrid.applications.component.CEAComponentManagerFactory">CEA interface self-test</a> for checking the CEA 
    interface, and </li>
    <li><a href="admin/sqlSyntaxTests.jsp">SQL syntax test</a> for checking that
    the ADQL-to-SQL translation being used is compatible with your RDBMS. 
    <br/>Note that the SQL syntax
    checking tests <strong>may take a long time to run</strong>,
    depending on the size of your dataset. </li>
</ul>

<p>
The DSA/catalog service comes with an embedded test database (HSQLDB) and 
pretend astronomical dataset for testing purposes, and the default 
configuration uses this test database.
</p>
<p>
<strong>
A newly-installed DSA/catalog service, running with the default 
installation configuration, should therefore complete all three sets of 
self-tests with no errors.
</strong>
</p>
<p>
Once you have configured access to your own RDBMS dataset instead of the 
embedded test database, you can use 
the self-test pages again to ensure that your configuration is complete
and correct.
</p>

<h2>Further checks</h2>

<p> If you are having problems getting your DSA/catalog installation
configured and working correctly, the links in the 'Service fingerprints'
menu can be used to further examine what java properties are being applied 
to the installation, and that these are as expected.
</p>

</body>
</html>
