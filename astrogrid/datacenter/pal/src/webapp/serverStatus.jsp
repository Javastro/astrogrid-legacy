<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.net.URL,
       org.astrogrid.io.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.service.HtmlDataServer"
   isThreadSafe="false"
   session="false"
%>
<%@ page language="java" %>
<%!
    HtmlDataServer server = new HtmlDataServer();
%>
<html>
<head><title>Datacenter Server Status</title></head>
<body>
<%
   
   URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/serverStatus.jsp");
   
   try {
      out.write(server.serverStatusAsHtml());
      out.write(server.makeRefreshSnippet(10, statusUrl.toString()));
   } catch (Throwable th) {
      LogFactory.getLog(request.getContextPath()).error(th);
      out.write(server.exceptionAsHtml("Getting server status", th));
   }
      

%>
</body>
</html>

