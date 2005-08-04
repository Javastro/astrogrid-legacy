<%@ page import="java.io.*,
      org.astrogrid.registry.server.query.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.util.DomHelper,
       org.apache.axis.utils.XMLUtils"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%>
<%@ page language="java" %><%
   RegistryQueryService server = new RegistryQueryService();
   String version = request.getParameter("version");
   Document entry = server.getRegistriesQuery(version);
   
   if (entry == null) {
      out.write("<Error>No entry returned</Error>");
   }
   else {
      //DomHelper.DocumentToWriter(entry, out);
      XMLUtils.ElementToWriter(entry.getDocumentElement(),out);
   }
%>