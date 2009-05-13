<%@ page import="java.io.*,
       org.w3c.dom.*,
       java.util.Date,
       org.astrogrid.io.*,
       org.astrogrid.dataservice.queriers.status.StatusLogger,
       org.astrogrid.cfg.ConfigFactory,
       org.astrogrid.xml.DomHelper,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title>Clearing Status Log on <%=DataServer.getDatacenterName() %></title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file='../header.xml' %>
<%@ include file='../navigation.xml' %>

<div id='bodyColumn'>
<h1>Status Log</h1>
<p>Clearing...
<%
   try {
      out.flush();
      new StatusLogger().clearStatusLog();
      out.write("...Cleared at "+new Date());
   } catch (Exception e) {
      out.println(ServletHelper.exceptionAsHtml("",  e, "Failed to clear status log\n" ));
   }
%>

</div>

<%@ include file='../footer.xml' %>
</body>
</html>


