<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       java.net.URL,
       org.w3c.dom.*,
       java.io.*,
       org.apache.commons.logging.*,
       org.astrogrid.store.Agsl,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.queriers.TargetIndicator,
       org.astrogrid.datacenter.query.AdqlQuery,
       org.astrogrid.datacenter.service.HtmlDataServer,
       org.astrogrid.io.*"
   isThreadSafe="false"
   session="false"
%><%@ page language="java" %><%!
    HtmlDataServer server = new HtmlDataServer();
%><%
   /*
    * Runs query from given ADQL/SQL string directly on server
    */
   String adqlSql = request.getParameter("AdqlSql");
   String resultsFormat = request.getParameter("Format");
   String resultsTarget = request.getParameter("Target");

   try {
      TargetIndicator target = null;
      if (resultsTarget.startsWith("mailto:")) {
         target = new TargetIndicator(resultsTarget.substring(7));
      }
      else {
         target = new TargetIndicator(new Agsl(resultsTarget));
      }
         
      String id = server.submitQuery(Account.ANONYMOUS, new AdqlQuery(adqlSql), target, resultsFormat);
      
      URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");
      //redirect to status
      out.write("ADQL/sql Query has been submitted, and assigned ID "+id+".  <a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
      
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Asking ADQL/sql: "+adqlSql, th));
   }

%>
