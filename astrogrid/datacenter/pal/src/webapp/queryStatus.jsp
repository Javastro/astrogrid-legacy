<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.net.URL,
       org.astrogrid.io.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.queriers.status.*,
       org.astrogrid.datacenter.service.HtmlDataServer"
   isThreadSafe="false"
   session="false"
%>
<%@ page language="java" %>
<%!
    HtmlDataServer server = new HtmlDataServer();
%>
<html>
<head><title>Status of Query
<%
   request.getParameter("ID");
%>
</title></head>
<body>
<%
   /*
    * Returns the status of the given query
    */
   String id = request.getParameter("ID");

   URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");

   //URL serverStatusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/serverStatus.jsp");

   try {
      QuerierStatus status = server.getQuerierStatus(Account.ANONYMOUS, id);
      out.write(server.queryStatusAsHtml(id, status));

      if (!(status instanceof QuerierClosed)) {
         //automatic refresh
         out.write(server.makeRefreshSnippet(3, statusUrl+"?ID="+id));
      }
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Getting status of query '"+id+"'", th));
   }
    

%>
</body>
</html>

