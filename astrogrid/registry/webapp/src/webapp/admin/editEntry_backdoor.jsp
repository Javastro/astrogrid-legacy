<%@ page import="org.astrogrid.registry.server.query.*,
                 org.w3c.dom.Document,
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.config.SimpleConfig,
 	  				  org.astrogrid.registry.server.http.servlets.helper.JSPHelper,                 
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Edit Registry Entry</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<%
   String resource = "";
   
   Document resourceDoc = null;
   if(request.getParameter("IVORN") != null && request.getParameter("IVORN").trim().length() > 0) {
        ISearch server = JSPHelper.getQueryService(request);
	     resourceDoc = server.getQueryHelper().getResourcesByIdentifier(request.getParameter("IVORN"));
	   if (resourceDoc != null) {
    	  resource = DomHelper.DocumentToString(resourceDoc);
	   }
	}
%>

<h1>Add/Update Entry</h1>
<p>
This page is similiar to Enter resource, but calls add/update method that the harvester uses in that it
updates any Resource even if it is not managed by this Registry. This is normally dangerous, you should let the 
harvester grab resources, but it can be usefull to quickly get in or repair a resource.
<font color='blue'>Warning this will use your current Contract Version-VOResource Version for the update, see your menu to 
check it is correct.</font>
</p>


<p>
<a href="../reg_xml_samples/updates">Sample area of xml for updates</a>
</p>

Upload from a local file:
<form enctype="multipart/form-data" method="post" action="addResourceEntry_backdoor.jsp">
<input type="file" name="docfile" />
<input type="hidden" name="addFromFile" value="true" />
<input type="submit" name="uploadFromFile" value="upload" />
</form>
<br />
Upload from a url:
<form method="post" action="addResourceEntry_backdoor.jsp">
<input type="text" name="docurl" />
<input type="hidden" name="addFromURL" value="true" />
<input type="submit" name="uploadFromURL" value="upload" />

</form>

Upload from text:<br />
<form action="addResourceEntry_backdoor.jsp" method="post">
<input type="hidden" name="addFromText" value="true" />
<p>
<textarea name="Resource" rows="30" cols="90">
<%= resource %>
</textarea>
</p>
<p>
<input type="submit" name="button" value="Submit"/>
</p>
</form>

</body>
</html>
