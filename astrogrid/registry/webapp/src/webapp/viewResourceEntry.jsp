<%@ page import="org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 org.apache.axis.utils.XMLUtils,
                 java.net.*,
                 java.util.*,
                 java.io.*"
   isThreadSafe="false"
   contentType="text/xml"                 
   session="false" %>
<%
   RegistryQueryService server = new RegistryQueryService();
   Document entry = server.getResourcesByIdentifier(request.getParameter("IVORN"), request.getParameter("version"));
   if (entry == null) {
       out.write("<Error>No entry returned</Error>");
   }
   else {
//    DomHelper.DocumentToWriter(entry, out);
      XMLUtils.ElementToWriter(entry.getDocumentElement(),out);
   }
%>