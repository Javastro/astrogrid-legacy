<%@ page
   import="org.astrogrid.dataservice.service.DataServer"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>PAL Documentation - Registering</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Registering your Datacenter</h1>

<p>First create the metadata 'Resource' documents as described <a href='resourcePlugins.jsp'>here</a>

<p>Then you need to insert the metadata into a Registry.
If you know of a Registry that you are always likely to use most often, then set the
<tt>org.astrogrid.registry.admin.endpoint</tt> configuration key to the suitable Service, eg:
</p>
<code>
org.astrogrid.registry.admin.endpoint=http://hydra.star.le.ac.uk:8080/astrogrid-registry-real/services/RegistryUpdate
</code>

<p>You can then use the <a href='../admin/register.jsp'>Registration</a> page to 'push' the
metadata to the Registry.

<p>Alternatively you can cut (from <a href='../GetMetadata'>here</a>) and paste
your metadata into the appropriate Registry's user interface pages.



</body>
</html>

