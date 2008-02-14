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
<h4><a href="./configure_step6.jsp">&nbsp;&nbsp;&lt;&lt; Previous step</a></h4>
</td>
<td align="center" width="33%">
<h4><a href="../configure.jsp">^Index^</a></h4>
</td>
<td align="right" width="33%">
<h4><font color="#999999">Next step &gt;&gt;&nbsp;&nbsp;</font></h4>
</td>
</tr>
</table>


<h2>Step 7: Register your DSA/catalog installation</h2>
<p>
Once you have completed 
<a href="./configure_step4.jsp">customising your configuration</a>, 
<a href="./configure_step5.jsp">preparing your metadoc file</a> and 
<a href="./configure_step6.jsp">re-running the self-tests</a>, 
you should be ready to register your DSA/catalog installation
so that other parts of the Virtual Observatory can be aware of it.
</p>

<h1>Registering your Datacenter</h1>

<p> 
The DSA-catalog installation has an administration page to help you create
and manage your registration metadata:
</p>

<h4>&nbsp;&nbsp;&nbsp;<a href='../admin/register.jsp'>Manage your DSA/catalog installation registrations&gt;&gt;</a></h4>

<p>Please follow the instructions on the page to register your DSA/catalog
installation at your chosen publishing registry.
</p>

<p>
Once your registration is completed, your DSA/catalog installation is 
complete - congratulations!
</p>

</body>
</html>
