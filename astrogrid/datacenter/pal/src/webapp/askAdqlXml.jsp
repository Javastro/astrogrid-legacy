<% // NB Don't allow any blank lines to be printed before the results %><%@
   page import="java.io.*,
       org.apache.commons.logging.*,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.service.*,
       org.astrogrid.datacenter.returns.*,
       org.astrogrid.datacenter.query.AdqlQuery,
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
   
   try {
     ReturnSpec returns = ServletHelper.makeReturnSpec(request);

     server.askQuery(Account.ANONYMOUS, new AdqlQuery(adqlXml), returns);
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(ServletHelper.exceptionAsHtmlPage("Asking ADQL/xml ", th, adqlXml));
   }

%>
