<%@ page import="java.io.*,
      java.net.URL,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.cfg.ConfigFactory,
       org.astrogrid.tableserver.test.SampleStarsPlugin,
       org.astrogrid.registry.client.RegistryDelegateFactory,
       org.astrogrid.dataservice.metadata.*,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title>Resources for <%=DataServer.getDatacenterName() %></title>
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
   // Initialise SampleStars plugin if required (may not be initialised
   // if admin has not run the self-tests)
   String plugin = ConfigFactory.getCommonConfig().getString(
        "datacenter.querier.plugin");
   if (plugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) {
      // This has no effect if the plugin is already initialised
      SampleStarsPlugin.initialise();  // Just in case
   }
   String table = MetadataHelper.getVosiEndpointsTable();
   out.write(table);
%>
</div>

<%@ include file='../footer.xml' %>
</body>
</html>
