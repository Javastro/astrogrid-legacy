<%@ page import="org.astrogrid.registry.server.JspHelper,
                 org.w3c.dom.Document,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Edit Registry Entry</title>
<style type="text/css" media="all">
          @import url("style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<%
   String resource = "";
   Document resourceDoc = JspHelper.getResource(request.getParameter("IVORN"));
   if (resourceDoc != null) {
      resource = DomHelper.DocumentToString(resourceDoc);
   }
%>

<h1>Add/Update Entry</h1>
<p>
Enter the VOResource in the text area below and press the [Submit] button to add it to the registry.  If the Resource
already exists, it will be updated.
</p>

Upload from a local file:
<form enctype="multipart/form-data" method="post" action="addResourceEntry.jsp">
<input type="file" name="docfile" />
<input type="hidden" name="addFromFile" value="true" />
<input type="submit" name="uploadFromFile" value="upload" />
</form>
<br />
Upload from a url:
<form method="post" action="addResourceEntry.jsp">
<input type="text" name="docurl" />
<input type="hidden" name="addFromURL" value="true" />
<input type="submit" name="uploadFromURL" value="upload" />
</form>

Upload from text:<br />
<form action="addResourceEntry.jsp" method="post">
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
