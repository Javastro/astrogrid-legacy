<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.net.URL,
       java.util.Date,
       org.astrogrid.io.*,
       org.astrogrid.status.*,
       org.astrogrid.dataservice.queriers.status.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.monitor.ServerStatusHtmlRenderer,
       org.astrogrid.dataservice.service.*,
       org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
   contentType="text/html"
   pageEncoding="UTF-8"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<%!
    DataServer server = new DataServer();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<%= ServletHelper.makeRefreshSnippet(10,
         new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/admin/serverStatus.jsp").toString()
      ) %>
<title><%= DataServer.getDatacenterName() %> Status</title>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
<style type="text/css" media="all">@import url("../style/astrogrid.css");</style>
</head>
<body>
<%@ include file="../header.xml" %>
<%@ include file="../navigation.xml" %>
<div id='bodyColumn'>

<h1><%= DataServer.getDatacenterName() %> Status <br/><%= new Date() %></h1>

<%
   ServerStatusHtmlRenderer renderer = new ServerStatusHtmlRenderer();
   
   TaskStatus[] persisted = null;
   /*  //Don't show the old history, it's just confusing
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
   */
   
   renderer.writeHtmlStatus(out, server.getStatus().getServiceStatus(), persisted); 
   %>
   <p>(Page self-refreshes every 10 seconds)</p>
</div>
</body>
</html>
