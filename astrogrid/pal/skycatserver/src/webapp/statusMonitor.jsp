<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.net.URL,
       java.util.Date,
       org.astrogrid.io.*,
       org.astrogrid.dataservice.service.ServletHelper,
       org.astrogrid.monitor.Monitor"
   isThreadSafe="false"
   session="false"
%>
<%!
    Monitor monitor = new Monitor();
%>

<html>
<head><title>Service Status Monitoring Page</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</head>
<body>
<div id='bodyColumn'>

<h1>Services Status Monitor  at <%= new Date() %></h1>

<% monitor.writeHtmlTables(new PrintWriter(out)); %>

<hr>

<%= ServletHelper.makeRefreshSnippet(30,
         new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/statusMonitor.jsp").toString()
      ) %>
</div>
</body>
</html>

