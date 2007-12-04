<%@ page
   import="org.astrogrid.dataservice.service.DataServer,
   org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = "."; // For the navigation include %>

<html>
<head>
<title>DSA/catalog Documentation</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Configuring your DSA/catalog installation</h1>
<p>
There are a number of steps involved in configuring your DSA/catalog installation; please work through them in sequence.
</p>
<ul>
<li><h3><a href="docs/configure_step1.jsp">Step 1 : Check your java and tomcat configuration</a></h3></li>
<li><h3><a href="docs/configure_step2.jsp">Step 2 : Test your default configuration</a></h3></li>
<li><h3><a href="docs/configure_step3.jsp">Step 3 : Prepare your RDBMS connection</a></h3></li>
<li><h3><a href="docs/configure_step4.jsp">Step 4 : Customise your configuration</a></h3></li>
<li><h3><a href="docs/configure_step5.jsp">Step 5 : Generate a skeleton metadoc file</a></h3></li>
<li><h3><a href="docs/configure_step5b.jsp">Step 5b: Customise your skeleton metadoc file</a></h3></li>
<li><h3><a href="docs/configure_step6.jsp">Step 6 : Test your customised configuration</a></h3></li>
<li><h3><a href="docs/configure_step7.jsp">Step 7 : Register your DSA/catalog with the VO</a></h3></li>
</ul>

</body>
</html>
