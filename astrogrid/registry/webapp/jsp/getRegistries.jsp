<%@ page import="org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.w3c.dom.Element,
                 org.w3c.dom.NodeList,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 org.apache.axis.utils.XMLUtils,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<%
   response.setContentType("text/xml");
   RegistryQueryService server = new RegistryQueryService();
   Document doc = server.GetRegistries(DomHelper.newDocument("<GetRegistries></GetRegistries>"));
   //System.out.println("back into the jsp try to println it first");
   //System.out.println("doc result = " + DomHelper.DocumentToString(doc));
   System.out.println("xmlutils = " + DomHelper.ElementToString(doc.getDocumentElement()));
   //DomHelper.DocumentToWriter(doc, out);
   XMLUtils.ElementToWriter(doc.getDocumentElement(),out);
%>