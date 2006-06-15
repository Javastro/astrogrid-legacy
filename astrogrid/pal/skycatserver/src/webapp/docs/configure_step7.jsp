<%@ page
   import="org.astrogrid.dataservice.service.DataServer,
   org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>

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
<%@ include file="navigation.xml" %>

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
The DSA-catalog installation has a self-registration function;
you can simply use the <a href='../admin/register.jsp'>Registration</a> 
page to 'push' the metadata to the Registry:
</p>

<h4>&nbsp;&nbsp;&nbsp;<a href='../admin/register.jsp'>Self-register your DSA/catalog installation &gt;&gt;</a></h4>


<p>
Alternatively, if you prefer, you can copy your registration XML metadata 
manually from the <a href='../GetMetadata'>XML Metadata page</a>) and paste
it into the appropriate Registry's user interface pages.  Note that
some browsers mangle XML when displaying it, so you should copy the
metadata from the page source (using e.g. your browser's "View page source"
option).
</p>

<p>
Once the registration has completed, you may wish to visit the Registry
where your data was submitted just to check that you can now see it there.
</p>

<p>
Once your registration is completed, your DSA/catalog installation is 
complete - congratulations!
</p>

</body>
</html>
