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
%><%
   /*
    * Returns the status of the given query
    */
   String id = request.getParameter("ID");

   URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/queryStatus.jsp");

   try {
      QuerierStatus status = server.getQuerierStatus(Account.ANONYMOUS, id);
      out.write(server.queryStatusAsHtml(id, status));

      if (!(status instanceof QuerierClosed)) {
         //automatic refresh
         out.write("<p><p>(Refreshes every 3 seconds)</p>");
         out.write("<META HTTP-EQUIV='Refresh' CONTENT='3;URL="+statusUrl+"?ID="+id+"'>");
      }
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Getting status of query '"+id+"'", th));
   }
    

%>

