<%@ page import="java.io.*,
       org.w3c.dom.*,
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

   try {
      out.write(server.getQueryStatus(Account.ANONYMOUS, id));
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Getting status of query '"+id+"'", th));
   }
    

%>

