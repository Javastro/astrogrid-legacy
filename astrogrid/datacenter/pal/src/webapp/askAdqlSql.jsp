<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       java.net.URL,
       org.w3c.dom.*,
       java.io.*,
       org.apache.commons.logging.*,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.query.AdqlQuery,
       org.astrogrid.datacenter.service.HtmlDataServer,
       org.astrogrid.io.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%@ page language="java" %><%!
    HtmlDataServer server = new HtmlDataServer();
%><%
   /*
    * Runs query from given ADQL/SQL string directly on server
    */
   String adqlSql = request.getParameter("AdqlSql");

   try {
     server.askQuery(Account.ANONYMOUS, new AdqlQuery(adqlSql), out, "VOTABLE");
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Asking ADQL/sql: "+adqlSql, th));
   }

%>
