<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.config.FailbackConfig,
       org.astrogrid.util.DomHelper,
       org.astrogrid.datacenter.metadata.*,
       org.astrogrid.datacenter.service.*"
   isThreadSafe="false"
   session="false"
%>
<html>
<head>
<title>Log Viewer for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("../style/maven-base.css");
          @import url("../style/maven-theme.css");
</style>
</title>
</head>

<body>
<%@ include file='../header.xml' %>
<%@ include file='../navigation.xml' %>

<div id='bodyColumn'>
<h1>Debug Log for <%=DataServer.getDatacenterName() %></h1>

<%
   String palDebugFilename = FailbackConfig.resolveEnvironmentVariables("${catalina.home}/logs/pal-debug.log");
   out.println("<p>From "+palDebugFilename+"</p>");
   out.println("<pre>");
   out.flush();
   FileReader logReader = new FileReader(palDebugFilename);
   Piper.bufferedPipe(logReader, out);
%>
</div>

<%@ include file='../footer.xml' %>
</body>
</html>


