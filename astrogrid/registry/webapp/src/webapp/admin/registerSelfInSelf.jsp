<%@ page import="org.astrogrid.registry.server.admin.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                  org.apache.axis.utils.XMLUtils,                 
                 java.io.*"
    session="false" %>

<html>
<head><title>Add Resource Entry</title>
</head>

<body>

<%
  String resource = request.getParameter("Resource");
  String postregsubmit = request.getParameter("postregsubmit");
  String getregsubmit= request.getParameter("getregsubmit");
  String getregs = request.getParameter("getregs");
  String fullRegistryAddURL = "http://hydra.star.le.ac.uk:8080/astrogrid-registry/addResourceEntry.jsp";
  //String regBas = request.getRequestURL().toString();
  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>

<h1>Adding Entry</h1>

<p>Service returns:

<pre>
<%
   Document result = null;
   RegistryAdminService server = new RegistryAdminService();
   if(resource != null && resource.trim().length() > 0) {
      result = server.updateResource(DomHelper.newDocument(resource));
      out.write("<p>Attempt at updating Registry, if any errors occurred it will be printed below<br /></p>");
      if (result != null) {
        DomHelper.DocumentToWriter(result, out);
      }
   } else {
      out.write("Error: no VOResource was entered.");
   }
%>
</pre>

<a href="index.html">Return to set-up index</a>

</body>
</html>
