<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.net.URL,
       java.util.Date,
       org.astrogrid.io.*,
       org.astrogrid.status.*,
       org.astrogrid.dataservice.queriers.status.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.monitor.ServerStatusHtmlRenderer,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>
<%!
    DataServer server = new DataServer();
%>
<html>
<head><title><%= DataServer.getDatacenterName() %> Status</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</head>
<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id='bodyColumn'>

<h1><%= DataServer.getDatacenterName() %> Status at <%= new Date() %></h1>

<%
   ServerStatusHtmlRenderer renderer = new ServerStatusHtmlRenderer();
   
   TaskStatus[] persisted = null;
      try {
         persisted = new StatusLogger().getPersistedLog();
      }
      catch (FileNotFoundException ioe) {
         //file probably doens't exist yet, or has been cleared
         server.getLog().warn(ioe+" looking for status log");
      }
      catch (IOException ioe) {
         out.write(ioe+" Reading status log");
         server.getLog().error("Reading Status Log", ioe);
      }
   
   renderer.writeHtmlStatus(out, server.getStatus().getServiceStatus(), persisted); 
   %>

<%= ServletHelper.makeRefreshSnippet(10,
         new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/serverStatus.jsp").toString()
      ) %>
</div>
</body>
</html>
