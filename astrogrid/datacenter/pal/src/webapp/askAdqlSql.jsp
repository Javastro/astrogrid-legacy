<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       java.net.URL,
       org.w3c.dom.*,
       java.io.*,
       org.apache.commons.logging.*,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.query.AdqlQuery,
       org.astrogrid.datacenter.sqlparser.Sql2Adql074,
       org.astrogrid.datacenter.service.*,
       org.astrogrid.datacenter.returns.*,
       org.astrogrid.io.*"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%@ page language="java" %><%!
    DataServer server = new DataServer();
%><%
   /*
    * Runs query from given ADQL/SQL string directly on server
    */
   String adqlSql = request.getParameter("AdqlSql");
   ReturnSpec returns = ServletHelper.makeReturnSpec(request);


   try {
     server.askQuery(Account.ANONYMOUS, new AdqlQuery(Sql2Adql074.translate(adqlSql)), returns);
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(ServletHelper.exceptionAsHtmlPage("Asking ADQL/sql: "+adqlSql, th));
   }

%>
