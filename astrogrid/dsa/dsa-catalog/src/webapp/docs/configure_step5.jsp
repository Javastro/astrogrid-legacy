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
<h4><a href="./configure_step4.jsp">&nbsp;&nbsp;&lt;&lt; Previous step</a></h4>
</td>
<td align="center" width="33%">
<h4><a href="../configure.jsp">^Index^</a></h4>
</td>
<td align="right" width="33%">
<h4><a href="./configure_step5b.jsp">Next step &gt;&gt;&nbsp;&nbsp;</a></h4>
</td>
</tr>
</table>


<h2>Step 5: Prepare your skeleton metadoc file</h2>
<p>
Once you have <a href="./configure_step4.jsp"</a>customised your configuration</a>, you need to prepare a "metadoc file" to describe your data.
</p>

<p>In order to publish to the Virtual Observatory you need to register your 
new service with a VO-compliant Registry.  This involves 'describing' your 
data, in a set of XML documents,
so that other VO tools and applications can examine it to see what it contains,
and so that they
know how to access the data.
</p>

<p>
An important part of this VO description is configured using a "metadoc" file,
  which describes the data being supplied by this DSA/catalog installation.
  This metadoc file is a bit like a database schema, but needs to contain
  additional information to help human astronomers and VO tooling make sense
  of your data.
  </p>

<p>
The metadoc file is an XML document;  
<a href="./TableMetaDoc.xsd">here is a copy of the formal schema</a> for this XML file format.
</p>

<h3>Generate a skeleton metadoc file</h3>
<p>
To assist you, this DSA/catalog installation can create a skeleton metadoc
file for you, using information extracted from your RDBMS:
</p>

<h4>&nbsp;&nbsp;&nbsp;<a href='../admin/GenerateMetaDoc'>Run the skeleton metadoc file generator &gt;&gt;</a></h4>

<p><strong>Note that this generator will only work if you have completed
<a href="./configure_step4.jsp">customising your configuration</a>, such that 
your DSA/catalog is now successfully configured to connect to your own RDBMS 
database.</strong>
</p>
<p>
If the generator fails, check your configuration settings.
The self-test pages may be of some help as well, although the self-tests
are NOT all expected to pass until you have produced a valid metadoc file.
</p>

<p>Also, note that your browser may mangle the XML produced by the metadoc
file generator when displaying it as a web page;  the safest way to extract 
the skeleton metadoc file is to view and save the page 
XML <strong>source</strong> (not the browser rendering of the XML source).
</p>

<p>
Once you have saved the skeleton metadoc file to a location of your choice
(preferably outside the DSA/catalog webapp), you should revisit your
configuration properties and set the <tt>datacenter.metadoc.file</tt> 
to point to it (see the notes in your properties file for more).
</p>
<p><strong>At this point, your DSA/catalog should be <a href="./configure_step6.jsp">passing all of its self-tests again</a>.</strong>
If it isn't, check your configuration file for misconfigured properties.
</p>
<p>
In <a href="./configure_step5b.jsp">the next step</a>, your skeleton 
metadoc file needs to be customised to be more informative.
</p>

</body>
</html>

