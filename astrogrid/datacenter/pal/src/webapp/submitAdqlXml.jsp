<% // NB Don't allow any blank lines to be printed before the results %><%@
   page import="java.io.*,
       java.net.URL,
       org.apache.commons.logging.*,
       org.astrogrid.store.Agsl,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.service.HtmlDataServer,
       org.astrogrid.datacenter.query.AdqlQuery,
       org.astrogrid.io.*"
   isThreadSafe="false"
   session="false"
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
      String id = server.submitQuery(Account.ANONYMOUS, new AdqlQuery(adqlXml), new Agsl(resultsTarget), resultsFormat);
      
      URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");

      //redirect to status
      out.write("Cone Query has been submitted, and assigned ID "+id+".  <a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Asking ADQL/xml ", th, adqlXml));
   }

%>
