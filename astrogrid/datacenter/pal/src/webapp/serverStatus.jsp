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
   try {
      DataServer.ServiceStatus status = server.getServerStatus();
      out.write(status.toHtml());
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Getting status of query '"+id+"'", th));
   }
      

%>

