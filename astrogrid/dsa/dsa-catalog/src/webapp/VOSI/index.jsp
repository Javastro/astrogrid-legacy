<%@ page import="java.io.*,
      java.net.URL,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.cfg.ConfigFactory,
       org.astrogrid.tableserver.metadata.TableMetaDocInterpreter,
       org.astrogrid.tableserver.test.SampleStarsPlugin,
       org.astrogrid.registry.client.RegistryDelegateFactory,
       org.astrogrid.dataservice.metadata.*,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>
<%
   String pathPrefix = "..";
   
   // Initialise SampleStars plugin if required (may not be initialised
   // if admin has not run the self-tests)
   String plugin = ConfigFactory.getCommonConfig().getString(
        "datacenter.querier.plugin");
   if (plugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) {
      // This has no effect if the plugin is already initialised
      SampleStarsPlugin.initialise();  // Just in case
   }
%>

<%
String[] ids = TableMetaDocInterpreter.getCatalogIDs();
String[] names = TableMetaDocInterpreter.getCatalogNames();
%>

<html>

<head>
<title>XML (VOSI) Resources</title>
<style type="text/css" media="all">@import url("../style/astrogrid.css");</style>
</head>

<body>
<%@ include file='../header.xml' %>
<%@ include file='../navigation.xml' %>

<div id='bodyColumn'>

<h1>XML (VOSI) Resources</h1>

<p>
  Here are metadata describing the service and its data. They are known as
  the VOSI (Virtual Observatory Support Interfaces) resources. You use
  these metadata when registering your service and clients can read them
  to work out how to drive the service.
</p>
<p>
  The resources come in two forms: split up by data collection or with all 
  collections aggregated. In DSA/Catalogue, "data collections" are "catalogues".
  If you only have one catalogue then the two forms are equivalent.
  If you have multiple catalogues, then you can choose whether to register
  one service for all of them or separate, virtual services for each catalogue.
</p>

<h2>System availability</h2>
<ul>
<li><a href="availability">All data collections</a></li>
</ul>
<p>
  The availability of a data collection is the same as the availability of
  a service, so there are no per-collection links.
</p>

<h2>Service capabilities</h2>
<ul>
<li><a href="capabilities">All data collections</a></li>
<%
for (int i = 0; i < ids.length; i++) {
%>
<li><a href="capabilities?COLLECTION=<%=names[i]%>"><%=names[i]%> (<%=ids[i]%>)</a></li>
<% 
}
%>
</ul>
<p>
  If you register with an AstroGrid publishing-registry, you pass it one of the
  links in this section when it asks for "URL for getting service capabilities".
  It will then find the other infomation for itself.
</p>

<h2>DB tables</h2>
<ul>
<li><a href="tables">All data collections</a></li>
<%
for (int i = 0; i < ids.length; i++) {
%>
<li><a href="tables?COLLECTION=<%=names[i]%>"><%=names[i]%> (<%=ids[i]%>)</a></li>
<%
}
%>
</ul>

<h2>CEA applications</h2>
<ul>
<%
for (int i = 0; i < ids.length; i++) {
%>
<li><a href="applications">All data collections</a></li>
<li><a href="applications?COLLECTION=<%=names[i]%>"><%=names[i]%> (<%=ids[i]%>)</a></li>
<%
}
%>
</ul>

</div>

<%@ include file='../footer.xml' %>
</body>
</html>
