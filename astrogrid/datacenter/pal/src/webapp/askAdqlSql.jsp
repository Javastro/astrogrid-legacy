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
   /*
    * Runs query from given ADQL/SQL string directly on server
    */
   String adqlSql = request.getParameter("AdqlSql");

   try {
     server.askAdqlSql(Account.ANONYMOUS, adqlSql, out);
   }
   catch (Exception e) {
      LogFactory.getLog(request.getContextPath()).error(e);
      out.write(server.exceptionAsHtml("Asking ADQL/sql: "+adqlSql, e));
   }

%>
