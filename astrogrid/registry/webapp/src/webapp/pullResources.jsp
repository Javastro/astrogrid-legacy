<%@ page import="org.astrogrid.registry.server.admin.*,
                 org.w3c.dom.Document,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Pull Resource Entry</title>
</head>

<body>

<%
  URL resourceUrl = new URL(request.getParameter("ResourceUrl"));
%>

<h1>Adding Entry</h1>
Adding resources at <%= resourceUrl %>

<p>Results of update:

<pre>
<%
   RegistryAdminService server = new RegistryAdminService();
   //Document entry = server.harvestFromResource(DomHelper.newDocument(resource));
   Document result = server.updateResource(DomHelper.newDocument(resourceUrl));
   if (result != null) {
      DomHelper.DocumentToWriter(result, out);
   }
   
%>
</pre>

</body>
</html>
