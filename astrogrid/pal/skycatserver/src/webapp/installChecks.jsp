<%@ page import="java.io.*,
         org.astrogrid.cfg.ConfigFactory,
         org.astrogrid.dataservice.service.DataServer"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>Publisher's Astrogrid Library for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>
<h1>Installation Checks</h1>
This page contains links to pages that will test your setup, and (should) show
some useful indication of what is the problem if there are any.
<p>

<h1>Test and Debug Axis</h1>
PAL uses Apache Axis as its underlying SOAP transport mechanism. These pages test
that axis is operating correctly:

<ul>
    <li><a href="happyaxis.jsp">Validate</a>
        the AXIS installation's configuration<br />
        <i>see below if this does not work.</i></li>
    <li><a href="servlet/AxisServlet">View</a>
        the list of deployed Web services</li>

</ul>

If the "happyaxis" validation page displays an exception instead of a
status page, the likely cause is that you have multiple XML parsers in
your classpath. Clean up your classpath by eliminating extraneous parsers.
</p>

<h1>Test and Debug PAL</h1>
<p>
The main test page for checking PAL configuration is below, and is also available
from the navigation bar on the left as 'self tests'.  These check various aspects
of an installation, and it might be that some failures are irrelevent.
<p>
The queries that the test submits to PAL are small southern hemisphere cone searches.  If you want to
define your own search to be used for the tests, set the property <tt>datacenter.testquery.sql</tt> to
the SQL you would like to run instead.
<p>
<ul>

<li>Run the PAL installation self-tests (see link on left)</li>
<li>Take a <a href="admin/fingerprint.jsp">fingerprint</a> of the datacenter installation</li>
</ul>
</p>

</body>
</html>

