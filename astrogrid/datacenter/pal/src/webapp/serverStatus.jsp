<% // NB Don't allow any blank lines to be printed before the results %><%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.service.HtmlDataServer"
   isThreadSafe="false"
   session="false"
%><%@ page language="java" %><%!
    HtmlDataServer server = new HtmlDataServer();
%><%
   try {
      out.write(server.serverStatusAsHtml());
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Getting server status", th));
   }
      

%>

