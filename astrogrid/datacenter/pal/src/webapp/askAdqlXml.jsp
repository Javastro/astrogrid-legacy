<% // NB Don't allow any blank lines to be printed before the results %><%@
   page import="java.io.*,
       org.apache.commons.logging.*,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.service.HtmlDataServer,
       org.astrogrid.datacenter.query.AdqlQuery,
       org.astrogrid.io.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%@ page language="java" %><%!
    HtmlDataServer server = new HtmlDataServer();
%><%
   /**
    * Runs ADQL query from given ADQL string
    */
   String adqlXml = request.getParameter("AdqlXml");
   String resultsFormat = request.getParameter("Format");
   String resultsTarget = request.getParameter("Target");
   
   try {
     server.askQuery(Account.ANONYMOUS, new AdqlQuery(adqlXml), out, resultsFormat);
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Asking ADQL/xml ", th, adqlXml));
   }

%>
