<%@ page
   import="org.astrogrid.dataservice.service.DataServer"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>PAL Documentation</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<!--
1 RELEASE document per component, to include:
Functionality supported
Known bugs and ommissions, with workarounds where possible
How to report bugs (BugZilla, content etc)
-->
<h1>Datacenter Release v1.2</h1>
<p>

<h2>Functionality Supported</h2>
<ul>
<li>Querying datacenters via ADQL 0.7.4 and region searches.
<li>CEA web service interfaces
<li>JSP pages, including a 
<!--
<a href="../queryBuilder-1-SelectFrom.jsp">query builder</a>
and 
-->
<a href="../adqlSqlForm.jsp">SQL</a> parser/submission form.
<li>Log views
<li>Metadata views
<li>Registry resource plugins
<li>Status views
<li>Partial SkyNode support
</ul>
</p>
<h2>Known bugs and Omissions</h2>
See bugzilla...
</p>
<h2>Reporting Bugs</h2>
<p>

Submit bugs to <a href="http://www.astrogrid.org/bugzilla/enter_bug.cgi">astrogrid's bugzilla</a>
Before filing a bug, we suggest you run the installation tests to verify that your system is correctly
configured.
<p />
Please include any error output or logs in the bug report, along with the server endpoint URL and version of the
datacenter server you have installed.
</p>

</p></div>
</body>
</document>
