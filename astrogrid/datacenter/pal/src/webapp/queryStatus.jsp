<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.queriers.status.*,
       org.astrogrid.datacenter.service.DataServer"
   isThreadSafe="false"
   session="false"
   contentType="text/xml"
%><%@ page language="java" %><%!
    DataServer server = new DataServer();
%><%
   /*
    * Returns the status of the given query
    */
   String id = request.getParameter("ID");

   try {
      QuerierStatus status = server.getQueryStatus(Account.ANONYMOUS, id);
      out.write(status.toString());
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Getting status of query '"+id+"'", th));
   }
      

%>

