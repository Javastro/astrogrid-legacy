<%@ page import="org.astrogrid.registry.server.harvest.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Viewing Registry Entry</title>
</head>

<body>

<%
  String resource = request.getParameter("Resource");
%>

<h1>Adding Entry</h1>

<pre>
<%
   RegistryHarvestService server = new RegistryHarvestService();

   Document entry = server.harvestFromResource(DomHelper.newDocument(resource));
   
   if (entry == null) {
      out.write("<p>No entry returned</p>");
   }
   else {
     DomHelper.DocumentToWriter(entry, out);
   }
%>
</pre>

</body>
</html>
