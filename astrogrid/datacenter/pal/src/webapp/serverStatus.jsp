<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.net.URL,
       java.util.Date,
       org.astrogrid.io.*,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
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
<%-- <p>Started: " <%= DataServer.startTime+"</p>"+ --%>

<h3>Memory</h3>
<p>
Free: <%= Runtime.getRuntime().freeMemory() %><br/>
Max:  <%= Runtime.getRuntime().maxMemory() %><br/>
Total:<%= Runtime.getRuntime().totalMemory() %><br/>
</p>

<h3>Load</h3>

<%
      URL statusUrl = new URL ("http",request.getServerName(),request.getServerPort(), request.getContextPath()+"/serverStatus.jsp");

      String[] running = server.getRunning();
      
      out.write("<p>Running Queries: "+ running.length+"</p>");

      for (int i=0;i<running.length;i++) {
         out.write("<a href='queryStatus.jsp?ID="+running[i]+"'>"+running[i]+"</a><br/>\n");
      }
   
      String[] done = server.getDone();

      out.write(
         "</p>"+
         "<h3>History</h3>"+
         "<p>Closed Queries: "+done.length+"</p>");

      for (int i=0;i<done.length;i++) {
         out.write("<a href='queryStatus.jsp?ID="+done[i]+"'>"+done[i]+"</a><br/>\n");
      }
%>
</p>
<%= ServletHelper.makeRefreshSnippet(10, statusUrl.toString()) %>
</div>
</body>
</html>

