<%@ page import="java.io.*,
      java.net.URL,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.cfg.ConfigFactory,
       org.astrogrid.registry.client.RegistryDelegateFactory,
       org.astrogrid.dataservice.metadata.*,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title>Register <%=DataServer.getDatacenterName() %></title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file='../header.xml' %>
<%@ include file='../navigation.xml' %>

<div id='bodyColumn'>

  <h1>XML Resources for <%= DataServer.getDatacenterName() %></h1>
<%
   String table = MetadataHelper.getVosiEndpointsTable();
   out.write(table);
%>
</div>

<%@ include file='../footer.xml' %>
</body>
</html>
