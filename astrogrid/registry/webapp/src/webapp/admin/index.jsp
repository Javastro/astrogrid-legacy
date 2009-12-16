<%@ page import="org.astrogrid.config.SimpleConfig"

   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>VAMDC Registry Setup Pages</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</title>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<h1>Setup and Administration of a VAMDC Registry</h1>

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
This page will allow you to add Indexes to your database, by default only indexes on text is done.  Click on this
link to add more indexes and a default set of indexes are shown that is commonly added to the VAMDC full registries.<br />
<a href="addIndexCheck.jsp">Click here</a> to add or update indexes.<br />
</p>

<h2>Initial Configuration</h2>
<p>You must 'self register' the Registry with itself in order for it to work.
You must also set the configuration property <code>org.vamdc.registry.authorityid</code> to
the authority that this Registry will manage. The second link for the custom sends you to the Add/Edit
entry page where you may upload a custom full Registry Type xml Resource.  Or you may elect to choose
an autmoated creator which is the first link.</p>
<ul>
<li><a href="selfRegisterForm.jsp">Self Registration</a></li>
<li><a href="editEntry.jsp">Self Registration Custom</a></li>
</ul>

<h2>Getting your Registry known</h2>
<p>
To get your registry known to the world you must register it with the 'Registry of Registries' (RofR) by placing in a url to what is known as a OAI
interface.  Go to: <br />
<a href="http://rofr.ivoa.net/regvalidate/regvalidate.html">RofR Validation and Registration</a>

All VAMDC registries have an OAI interface url and for this registry it should be 
(Note if your on a proxy system the below url might not be correct please correct it to the correct url/domain):
<br />
<%= request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath() %>/OAIHandlerv1_0
<br /><br />


<h2>Harvesting other Registries</h2>
<p>To make your registry harvest other registries - you must get the other known Registry type Resources.
This is typically done by getting all the Registry Type Resources from Registry of Registries (RofR).  Click on the link below to do this.</p>
<ul>
<li><a href="getRegistryFromHarvest.jsp">Harvesting other Registries</a></li>
</ul>

</div>
<%@ include file="/style/footer.xml" %>

</body>
</html>
