<%@ page import="org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Query</title>
</head>

<body>
<h1>Query Registry</h1>

<p>
	You can submit ADQL 0.73 or 0.74 adql queries.<br />
</p>

<%
  String resource = request.getParameter("Resource");
%>
<br />
<strong>Only a max of 25 entries will be shown:</strong><br />

<pre>
<%
	   Document adql = DomHelper.newDocument(resource);
	   RegistryQueryService server = new RegistryQueryService();
   
	   Document entry = server.Search(adql);
	   if (entry == null) {
    	  out.write("<p>No entry returned</p>");
	   }
	   else {
    	 DomHelper.DocumentToWriter(entry, out);
	   }
%>
</pre>

<a href="registry_index.html">Index</a>

</body>
</html>
