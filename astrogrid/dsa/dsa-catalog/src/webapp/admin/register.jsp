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
<style type="text/css" media="all">@import url("../style/astrogrid.css");</style>
</head>

<body>
<%@ include file='../header.xml' %>
<%@ include file='../navigation.xml' %>

<div id='bodyColumn'>
  <h1>Registering <%= DataServer.getDatacenterName() %></h1>

  <p>
  The controls below allow you to register or update your DSA/catalog
  installation in an AstroGrid registry.  If you are using
  a registry implementation from a different provider, you will need to 
  look at that registry's documentation to work out how to create and update
  registrations.
  </p>
  <p>
    <strong>Read the notes below before pushing any buttons.</strong>
  </p>
<%
// Optionally enable registration (DISABLED when SampleStarsPlugin in use)
  String qPlugin = ConfigFactory.getCommonConfig().getString("datacenter.querier.plugin");
  if ( (qPlugin != null) && (qPlugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) ) {
%>
%>
  <p>
    Registration is not supported for DSA/Catalog installations
    using the SampleStars database.  Please configure this installation
    to use your own data; registration controls will then appear in place 
    of this notice.
  </p>
<%
} else {
%>
<%=MetadataHelper.getRegisterUpdateTable()%>
<%
}
%>
<p>
You can register in two ways:
</p>
<ul>
  <li>all catalogues registered as a single service (normal);</li>
  <li>each catalogue registered as a separate, virtual service (special cases);</li>
</ul>
<p>
  (the "virtual" services are just additional registrations, under different
  IVOA identifiers, of your DSA installation; you don't have to install
  any extra software.)
</p>
<p>
  Catalogues registered separately are simpler and easier for end-users to
  understand, but do not allow joins and cross-matches between tables.
  Catalogues registered together allow cross-catalogue joins: this is more
  useful but more complex for the user.
</p>
<p>
  You can register in both modes: all catalogues together <em>and</em>
  individual catalogues. In this case, you can choose a sub-set of your
  catalogues to expose as separate services. The first row in the table of
  controls above registers all your catalogues as one service. The subsequent
  rows each register one catalogue as a virtual service.
</p>
<p>
  You <em>should</em> register all catalogues on one service <em>unless</em>
  you specifically want to block joins and cross-matches between catalogues.
  If you only have one catalogue then <em>only</em> register "all catalogues";
  making a virtual service won't help in this case.
</p>
<p>
  When you register for the first time, you will see a "Register new resource"
  button. When you have a catalogue already registered you will see instead
  buttons marked "Edit core metadata" and "Load service metadata". "Core"
  metadata describe the scientific use of your service while "service" metadata
  are the technical details of how to drive the service. You have to type in
  the core metadata, but the registry reads the service metadata from your
  DSA service (see the <a href="../VOSI">VOSI</a> page for details of the service
  metadata). The controls send your browser to pages inside the registry; when you
  come back to the DSA pages, refresh your browser to see the correct view.
  You can alter the core metadata and reload the service metadata as often as
  needed; the new information replaces the old. If you alter the configuration
  of your DSA installation then you should reload the service metadata.
</p>
<p>
  If you are prompted for a username and password when using the
above links, please consult the administrator of your publishing registry to 
obtain a username and password.
</p>

</div>

<%@ include file='../footer.xml' %>
</body>
</html>
