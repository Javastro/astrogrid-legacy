<%@ page import="org.astrogrid.registry.server.admin.*,
                 org.w3c.dom.Document,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Pull Resource Entry</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>

</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<%
  URL resourceUrl = new URL(request.getParameter("ResourceUrl"));
%>

<h1>Adding Entry</h1>
Adding resources at <%= resourceUrl %>

<p><b>Server Response:</b>

<pre>
<%
   RegistryAdminService server = new RegistryAdminService();
   //Document entry = server.harvestFromResource(DomHelper.newDocument(resource));
   Document result = server.updateResource(DomHelper.newDocument(resourceUrl));
   if (result != null) {
      out.write(DomHelper.DocumentToString(result).replaceAll("<","&lt;").replaceAll(">","&gt;"));
   }
   
%>
</pre>

</body>
</html>
