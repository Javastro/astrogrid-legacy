<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Setup Pages</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="../admin/navigation.xml" %>

<div id='bodyColumn'>

<h1>Setup Configuration of an AstroGrid Registry</h1>

<h2>Pre-Requisits</h2>
<p>We assume that you have already carried out the following tasks:
<ul>
<li>Installed the Registry WAR - that is, that you are viewing these pages on
your browser on the site you have installed the WAR file on.
<li>Installed the eXist database
</ul>

<h2>Initial Configuration</h2>
<p>You must 'self register' the Registry with itself in order for it to work.
You must also set the configuration property <code>org.astrogrid.registry.authorityid</code> to
the authority that this Registry will manage.
<ul>
<li><a href="selfRegisterForm.jsp">Self Registration</a>
</ul>
<p>
<p>To have your registry mirrored by other registries - for them to harvest this
one to pick up resources that have been registered on it - you must copy
your registryType entry into those registries.
<ul>
<li><a href="../admin/registerSelfInExternalRegistry.html">Register with other Registries</a>
</ul>

<p>To make your registry mirror other registries - for it to 'harvest' them regularly
to pick up resources that have been registered on them - you must copy their
registryType entries into your registry.

<ul>
<li><a href="../admin/registerExternalRegistryInSelf.html">Harvesting other Registries</a>
</ul>

<h2>Populating your Registry</h2>
<p>Add new entries using the <a href='editEntry.jsp'>Entry/Update Form</a>



</body>
</html>

