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
  <h1>Registering <%= DataServer.getDatacenterName() %></h1>

<%
// Optionally enable registration (DISABLED when SampleStarsPlugin in use)
   String qPlugin = ConfigFactory.getCommonConfig().getString(
        "datacenter.querier.plugin");
   if ( (qPlugin != null) && (!qPlugin.equals("org.astrogrid.tableserver.test.SampleStarsPlugin")) ) {
%>
  <p>
  To conform with new IVOA standards, you must manually create the 
  registration(s) for your DSA/catalog installation at your chosen 
  publishing registry.
  </p>
  <p>
  The following links will allow you to register or update your DSA/catalog
  installation on an <strong>AstroGrid</strong> registry.  If you are using 
  a registry implementation from a different provider, you will need to 
  look at that registry's documentation to work out how to create / update 
  registrations.</p>

<%
   String table = MetadataHelper.getRegisterUpdateTable();
   out.write(table);
%>
<p>Note 1:  The links in the table above will open a Registry 
page in a new browser window.  You may need to manually refresh this 
page after editing details at the registry, to see updated links in the 
table above.</p>

<p>Note 2: If you are prompted for a username and password when using the 
above links, please consult the administrator of your publishing registry to 
obtain a username and password.
</p>

<h2>Further information</h2>

<ul>
<li><p>If your service is not yet registered, you will see one or more 
<strong><tt>Register now</tt></strong> links in the second column above.  
These take you to a registration form on your chosen AstroGrid 
publishing registry, allowing you to create the necessary new registration(s)
for each catalog/database that is wrapped by this DSA/catalog service.  
</p></li>

<li><p>If your service is already registered, you will see one or more 
<strong><tt>Edit core metadata</tt></strong> links in the third column above.  
These allow you to edit and update the Dublin Core (human-readable) 
metadata for this DSA/catalog service.
</p></li>

<li><p>If your service is already registered, you will see one or more 
<strong><tt>Force refresh</tt></strong> links in the fourth column above.  
These allow you to force your chosen AstroGrid publishing registry to refresh
the system-related metadata it has previously harvested from the DSA/catalog
service.  Use this if you change any configuration settings on your 
DSA/catalog installation, to ensure that the registry has an up-to-date
view.
</p></li>
</ul>
<%
}
else {
%>
  <p>
  Registration is not supported for DSA/Catalog installations
  using the SampleStars database;  please configure this installation
  to use your own data before registering it. 
  </p>
<%
}
%>
</div>

<%@ include file='../footer.xml' %>
</body>
</html>
