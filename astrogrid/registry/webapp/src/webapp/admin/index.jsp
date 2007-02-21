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

<h2>Indexes</h2>
<p>
Located here is something new whereby you can manage the indexes in the database.  
This page will allow you to
add Indexes to your database, again there is a different index page for different "Contract" versions such
as the 1.0 and 0.10 Resources.<br />
<a href="addIndexCheck.jsp">Click here</a> to add or update indexes.<br />
</p>

<h2>Initial Configuration</h2>
<p>You must 'self register' the Registry with itself in order for it to work.
You must also set the configuration property <code>org.astrogrid.registry.authorityid</code> to
the authority that this Registry will manage. The second link for the custom sends you to the Add/Edit
entry page where you may upload a custom full Registry Type xml Resource.  Or you may elect to choose
an autmoated creator which is the first link.</p>
<ul>
<li><a href="selfRegisterForm.jsp">Self Registration</a></li>
<li><a href="editEntry.jsp">Self Registration Custom</a></li>
</ul>

<h2>Getting your Registry known</h2>
<p><font color='blue'>Warning</font> At this time RofR has only recently came up in the last month and currently has no interface for submitting your Registry Type.
Currently suggest e-mailing the url to your OAI Identify to the <a href='mailto:registry@ivoa.net'>registry@ivoa.net</a>.  <br />
Your HTTP-GET OAI url corresponding to Registry Interface version 1.0 is: <br />
<%= request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath() %>/OAIHandlerv1_0
<br /><br />
<font color='red'>For 0.10 only</font>To have your registry harvested by other registries you must copy
your registryType entry into those registries. (This is optional)</p>
<ul>
<li><a href="registerSelfInExternalRegistry.jsp">Register with other Registries</a></li>
</ul>

<h2>Harvesting other Registries</h2>
<p>To make your registry harvest other registries - you must get the other known Registry type Resources.
This is typically done by getting all the Registry Type Resources from Registry of Registries (RofR).  Click on the link below to do this.</p>
<ul>
<li><a href="getRegistryFromHarvest.jsp">Harvesting other Registries</a></li>
</ul>



</body>
</html>