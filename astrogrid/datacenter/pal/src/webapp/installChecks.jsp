<%@ page import="java.io.*,
         org.astrogrid.config.SimpleConfig,
         org.astrogrid.datacenter.service.DataServer"
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

<h1>Test and Debug Axis</h1>
PAL uses Apache Axis as its underlying SOAP transport mechanism.

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
<ul>

<li><a href="TestServlet?suite=org.astrogrid.datacenter.service.InstallationSelfCheck">Run</a> the PAL installation
self-tests</li>
<li>Take a <a href="fingerprint.jsp">fingerprint</a> of the datacenter installation</li>
</ul>
</p>

</body>
</html>

