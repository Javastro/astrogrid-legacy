<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.util.DomHelper,
       org.astrogrid.registry.server.JspHelper"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%@ page language="java" %><%
   Document entry = JspHelper.getResource(request.getParameter("IVORN"));
   
   if (entry == null) {
      out.write("<Error>No entry returned</Error>");
   }
   else {
      DomHelper.DocumentToWriter(entry, out);
   }
%>

