<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Setup Pages</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</title>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<h1>Setup and Administration of an AstroGrid Registry</h1>

<h2>Pre-Requisits</h2>
<p>We assume that you have already carried out the following tasks:</p>
<ul>
<li>Installed the Registry WAR - that is, that you are viewing these pages on
your browser on the site you have installed the WAR file on.
<li>Installed the eXist database
</ul>

<h2>Initial Configuration</h2>
<p>You must 'self register' the Registry with itself in order for it to work.
You must also set the configuration property <code>org.astrogrid.registry.authorityid</code> to
the authority that this Registry will manage. The second link for the custom sends you to the Add/Edit
entry page where you may upload a custom full Registry Type xml Resource.  Or you may elect to choose
an autmoated creator which is the first link.</p>
<ul>
<li><a href="selfRegisterForm.jsp">Self Registration</a></li>
<li><a href="../editEntry.jsp">Self Registration Custom</a></li>
</ul>
<p>
<p>To have your registry mirrored by other registries - for them to harvest this
one to pick up resources that have been registered on it - you must copy
your registryType entry into those registries. (This is optional)</p>
<ul>
<li><a href="registerSelfInExternalRegistry.jsp">Register with other Registries</a></li>
</ul>

<p>To make your registry mirror other registries - for it to 'harvest' them regularly
to pick up resources that have been registered on them - you must copy their
registryType entries into your registry. (This is optional)</p>

<ul>
<li><a href="registerExternalRegistryInSelf.jsp">Harvesting other Registries</a></li>
</ul>

<h2>Populating your Registry</h2>
<p>Add new entries using the <a href='editEntry.jsp'>Entry/Update Form</a> </p>



</body>
</html>