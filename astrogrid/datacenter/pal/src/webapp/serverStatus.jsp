<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.net.URL,
       java.util.Date,
       org.astrogrid.io.*,
       org.astrogrid.datacenter.queriers.status.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.status.SelfMonitorBody,
       org.astrogrid.datacenter.service.*"
   isThreadSafe="false"
   session="false"
%>
<%!
    DataServer server = new DataServer();
%>
<html>
<head><title>Status of <%= DataServer.getDatacenterName() %></title>
<style type="text/css" media="all">
          @import url("./style/maven-base.css");
          @import url("./style/maven-theme.css");
</style>
</head>
<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id='bodyColumn'>

<h1>Datacenter Service Status at <%= new Date() %></h1>

<% SelfMonitorBody.writeHtmlStatus(out, server.getStatus().getServiceStatus()); %>

<%= ServletHelper.makeRefreshSnippet(10,
         new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/serverStatus.jsp").toString()
      ) %>
</div>
</body>
</html>

