<%@ page import="java.io.*,
      org.astrogrid.registry.server.query.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.util.DomHelper,
       org.apache.axis.utils.XMLUtils,
       org.astrogrid.registry.server.JspHelper"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%>
<%@ page language="java" %><%
   RegistryQueryService server = new RegistryQueryService();
   Document entry = server.GetRegistries(DomHelper.newDocument());
   
   if (entry == null) {
      out.write("<Error>No entry returned</Error>");
   }
   else {
      //DomHelper.DocumentToWriter(entry, out);
      XMLUtils.ElementToWriter(entry.getDocumentElement(),out);
   }
%>