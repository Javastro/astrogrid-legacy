<%@ page import="org.astrogrid.registry.server.admin.*,
                 org.w3c.dom.*,
                 org.astrogrid.registry.server.JspHelper,
                 org.astrogrid.util.DomHelper,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Delete Resource</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>
<h1>Delete Resource</h1>

<% String ivorn = request.getParameter("IVORN"); %>

<p>Deleting Resource identified by '<%= ivorn %>'
<p>
<%
   if ((ivorn == null) || (ivorn.trim().length() == 0)) {
      out.write("No IVORN given to delete (Parameter 'IVORN' empty)");
   }
   else {
      Document resourceDoc = JspHelper.getResource(ivorn);
      if (resourceDoc == null) {
         out.write("Resource not found");
      }
      else {
         Element resource = (Element) resourceDoc.getDocumentElement().getElementsByTagNameNS("*","Resource").item(0);
         resource.setAttribute("status", "deleted");
         
         RegistryAdminService server = new RegistryAdminService();
         Document serverResponse = server.updateResource(resourceDoc);
         out.write("<b>Server Response:</b><p><pre>");
         if (serverResponse != null) {
            out.write(DomHelper.DocumentToString(serverResponse).replaceAll("<","&lt;").replaceAll(">","&gt;"));
         }
         out.write("</pre>");
      }
   }
   
%>

</body>
</html>
