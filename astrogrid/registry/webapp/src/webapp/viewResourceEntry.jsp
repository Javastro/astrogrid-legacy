<%@ page import="org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
  	             org.astrogrid.registry.server.http.servlets.helper.JSPHelper,                 
                 org.w3c.dom.Document,
                 org.w3c.dom.Element,
                 org.w3c.dom.NodeList,
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
   ISearch server = JSPHelper.getQueryService(request);
   Document entry = server.getQueryHelper().getResourceByIdentifier(request.getParameter("IVORN"));
   NodeList nl = entry.getElementsByTagNameNS("*","Resource");
   
   if (entry == null || nl.getLength() == 0) {
       out.write("<Error>No entry returned</Error>");
   } else {
      XMLUtils.ElementToWriter((Element)nl.item(0),out);
   }
   
%>
