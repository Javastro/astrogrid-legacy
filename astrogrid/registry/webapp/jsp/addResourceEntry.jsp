<%@ page import="org.astrogrid.registry.server.admin.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Add Resource Entry</title>
</head>

<body>

<%
  String resource = request.getParameter("Resource");
%>

<h1>Adding Entry</h1>

<p>Service returns:

<pre>
<%
   //RegistryHarvestService server = new RegistryHarvestService();
   RegistryAdminService server = new RegistryAdminService();

   //Document entry = server.harvestFromResource(DomHelper.newDocument(resource));
   Document entry = server.updateResource(DomHelper.newDocument(resource));
   
   if (entry == null) {
      out.write("<p>No entry returned</p>");
   }
   else {
     DomHelper.DocumentToWriter(entry, out);
   }
%>
</pre>

<a href="index.html">Index</a>

</body>
</html>
