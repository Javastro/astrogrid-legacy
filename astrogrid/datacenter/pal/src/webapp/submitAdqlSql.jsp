<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       java.net.URL,
       org.w3c.dom.*,
       java.io.*,
       org.apache.commons.logging.*,
       org.astrogrid.store.Agsl,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.sqlparser.Sql2Adql05,
       org.astrogrid.datacenter.service.*,
       org.astrogrid.datacenter.returns.*,
       org.astrogrid.datacenter.query.AdqlQuery,
       org.astrogrid.io.*"
   isThreadSafe="false"
   session="false"
%><%@ page language="java" %><%!
    DataServer server = new DataServer();
%><%
   /*
    * Runs query from given ADQL/SQL string directly on server
    */
   String adqlSql = request.getParameter("AdqlSql");

   try {
      ReturnSpec returns = ServletHelper.makeReturnSpec(request);

      if (returns.getTarget() == null) {
         returns.setTarget(new TargetIndicator(out));
         server.askQuery(Account.ANONYMOUS, new AdqlQuery(Sql2Adql05.translate(adqlSql)), returns);
      }
      else {
         String id = server.submitQuery(Account.ANONYMOUS, new AdqlQuery(adqlSql), returns);

         URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");
         //indicate status
         out.write("Adql Query has been submitted, and assigned ID "+id+".  <a href='"+statusUrl+"?ID="+id+"'>Query Status Page</a>\n");
         //redirect to status
         out.write("<META HTTP-EQUIV='Refresh' CONTENT='0;URL="+statusUrl+"?ID="+id+"'>");
      }
   }
   catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(ServletHelper.exceptionAsHtmlPage("Asking ADQL/sql: "+adqlSql, th));
   }

%>
