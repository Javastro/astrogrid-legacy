<%@ page import="org.astrogrid.registry.server.query.*,
                 org.w3c.dom.*,
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
    	  resource = DomHelper.ElementToString(((Element)(resourceDoc.getDocumentElement().getFirstChild())));
	   }
	}
%>

<h1>Add/Update Entry</h1>
<p>
Here you can update the resources in various ways. If the Resources are already there then it will be updated, This page also can handle multiple versions of the registry to be updated:
<%
if(SimpleConfig.getSingleton().getBoolean("reg.amend.validate",false)) {
%>
<br />
<font color="blue">Validation is already turned on for server side updates.</font><br />
<%
   if(SimpleConfig.getSingleton().getBoolean("reg.amend.quiton.invalid",false)) {
%>
      <font color="blue">It is also set to Quit on invalid updates.</font>
<%}else {%>
      <font color="blue">The server side though will still attempt to update the invalid document.</font>
<%}%>


<%
}else {
%>
<br />
<font color="blue">Validation is <font color="red">Not</font> turned on for server side updates.</font>
<%}%>
</p>

<p>
<a href="reg_xml_samples/updates">Sample area of xml for updates</a>
</p>

Upload from a local file:
<form enctype="multipart/form-data" method="post" action="addResourceEntry.jsp">
<input type="checkbox" name="validate" value="true">Validate</input>
<input type="file" name="docfile" />
<input type="hidden" name="addFromFile" value="true" />
<input type="submit" name="uploadFromFile" value="upload" />
</form>
<br />
Upload from a url:
<form method="post" action="addResourceEntry.jsp">
<input type="checkbox" name="validate" value="true">Validate</input>
<input type="text" name="docurl" />
<input type="hidden" name="addFromURL" value="true" />
<input type="submit" name="uploadFromURL" value="upload" />

</form>

Upload from text:<br />
<form action="addResourceEntry.jsp" method="post">
<input type="checkbox" name="validate" value="true">Validate</input>
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
