<%@ page import="org.astrogrid.registry.server.admin.*,
                 org.w3c.dom.Document,
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
   String forwardTo = request.getParameter("forwardTo");

   if ((ivorn == null) || (ivorn.trim().length() == 0)) {
      out.write("No IVORN given to delete (Parameter 'IVORN' empty)");
   }
   else {
      out.write("<b>Server Response:</b><p><pre>");
//      RegistryAdminService server = new RegistryAdminService();
      out.write("Not implemented yet");
      out.write("</pre>");

      //forward if everything has gone OK (?)
      if ((forwardTo != null) && (forwardTo.trim().length() != 0) ) {
         out.write("<p>Forwarding to "+forwardTo);
         out.flush();
         request.getRequestDispatcher(forwardTo).forward(request, response);
      }
   
   }
   
%>

</body>
</html>
