<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       java.net.URL,
       org.w3c.dom.*,
       java.io.*,
       org.apache.commons.logging.*,
       org.apache.axis.utils.XMLUtils,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.adql.ADQLUtils,
       org.astrogrid.datacenter.service.DataServer,
       org.astrogrid.util.DomHelper,
       org.astrogrid.io.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%@ page language="java" %><%!
    DataServer server = new DataServer();
%><%
   /**
    * Runs ADQL query from given ADQL string
    */
   Document adqlXml = DomHelper.newDocument(new StringBufferInputStream(request.getParameter("AdqlXml")));
   
   
   /* Direct server call - and why not? */
   try {
     Document votable = server.askAdql(Account.ANONYMOUS, ADQLUtils.unmarshalSelect(adqlXml)).toVotable();
      
     XMLUtils.DocumentToWriter(votable, out);
      
   } catch (Exception e) {
      LogFactory.getLog("searchCone").error("askAdqlXml failed", e);
      //this should probably be a VOtable error...
      out.println("<html>");
      out.println("<head><title>Ask ADQL XML Failed</title></head>");
      out.println("<body>");
      out.println("<H1>SERVER ERROR</H1>");
      out.println("<pre>");
      e.printStackTrace(new PrintWriter(out));
      out.println("</pre>");
      out.println("</body>");
      out.println("</html>");
   }

%>
