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
   String adqlXml = request.getParameter("AdqlXml");
   Document adqlDom = DomHelper.newDocument(new StringBufferInputStream(adqlXml));
   
   try {
     server.askAdql(Account.ANONYMOUS, ADQLUtils.unmarshalSelect(adqlDom), out);
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Asking ADQL/sql ", th, adqlXml));
   }

%>
