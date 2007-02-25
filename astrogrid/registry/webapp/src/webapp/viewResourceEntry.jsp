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
   Document entry;   try {   entry = server.getQueryHelper().getResourceByIdentifier(request.getParameter("IVORN"));
   }catch(Exception e) {
    entry = null;
   }

   if(entry == null) {
       out.write("<Error>No entry returned</Error>");
   }else {
	   NodeList nl = entry.getElementsByTagNameNS("*","Resource");
	   if (nl.getLength() == 0) {
	       out.write("<Error>No entry returned</Error>");
	   } else {
	      XMLUtils.ElementToWriter((Element)nl.item(0),out);
	   }
 	}

   
%>
