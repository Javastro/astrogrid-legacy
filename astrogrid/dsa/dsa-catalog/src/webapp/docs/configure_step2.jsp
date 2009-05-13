<%@ page
   import="org.astrogrid.dataservice.service.DataServer,
   org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title>DSA/catalog Documentation</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="../navigation.xml" %>

<div id='bodyColumn'>

<h1>Configuring your Datacenter</h1>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
<td align="left" width="33%">
<h4><a href="./configure_step1.jsp">&nbsp;&nbsp;&lt;&lt; Previous step</a></h4>
</td>
<td align="center" width="33%">
<h4><a href="../configure.jsp">^Index^</a></h4>
</td>
<td align="right" width="33%">
<h4><a href="./configure_step3.jsp">Next step &gt;&gt;&nbsp;&nbsp;</a></h4>
</td>
</tr>
</table>


<h2>Step 2: Test your default configuration</h2>
<p>
By default, the DSA/catalog installation is configured to run against its
own test database (powered by an embedded HSQLDB server).  
</p>

<p>
The self-test links found in the 
'Self-tests' sidebar menu should all run without error when the DSA/catalog is
freshly installed in its out-of-the-box configuration.
</p>

<p>
In summary, the self-test functions are as follows:
</p>

<ul>
    <li><a href="../admin/happyaxis.jsp">Apache Axis self-check</a>
    for validating the AXIS installation's configuration.</a><br/>
    DSA/catalog uses Apache Axis as its underlying SOAP transport mechanism. 
    It is not necessarily problematic if "optional components" are missing.
    If this validation page displays an exception instead of a status 
    page, the likely cause is that you have multiple XML parsers in your 
    classpath.
    </li>
   <li> <a href="../admin/TestServlet?suite=org.astrogrid.dataservice.service.InstallationPropertiesCheck">Initial properties check</a> which checks that all expected configuration properties are defined (though it doesn't check them for correctness).</li> 
   <li> <a href="../admin/TestServlet?suite=org.astrogrid.dataservice.service.InstallationSelfCheck">Installation self-test</a> for checking basic service configuration,</li> 
    <li><a href="../admin/TestServlet?suite=org.astrogrid.applications.component.CEAComponentManagerFactory">CEA interface self-test</a> for checking the CEA 
    interface, and </li>
    <li><a href="../admin/sqlSyntaxTests.jsp">SQL syntax test</a> for checking that the ADQL-to-SQL translation being used is compatible with your RDBMS.
    <br/>Note that the SQL syntax
    checking tests <strong>may take a long time to run</strong>,
    depending on the size of your dataset. If your browser times out waiting
    for the tests to finish, you can watch their progress in the 
    <tt>Server status</tt> page (click on the <tt>View server status</tt> link
        in the left sidebar).   When no new self-test queries appear, the 
        tests have finished.</li>
</ul>

</body>
</html>


