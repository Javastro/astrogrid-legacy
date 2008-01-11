<%@ page import="java.io.*,
      java.net.URL,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.cfg.ConfigFactory,
       org.astrogrid.registry.client.RegistryDelegateFactory,
       org.astrogrid.dataservice.metadata.*,
       org.astrogrid.dataservice.service.*,
       org.astrogrid.dataservice.service.servlet.Register"
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

<body>

  <h1>Registering <%= DataServer.getDatacenterName() %></h1>

  <p>
  To conform with new IVOA standards, you must manually create the 
  registration(s) for your DSA/catalog installation at your chosen 
  publishing registry.
  </p>
  <p>
  The following instructions will help you to register this DSA/catalog
  installation on an <strong>AstroGrid</strong> registry.  If you are using 
  a registry implementation from a different provider, you will need to 
  look at that registry's documentation to work out how to create / update 
  registrations.</p>

<div style="background:yellow; padding:5px">
<p>
<font color="red"><b>INSTRUCTIONS TO FOLLOW IN SUBSEQUENT RELEASE.<br/>
THIS RELEASE OF DSA IS A POINT-RELEASE FOR IN-HOUSE TESTING
 ONLY!  DO NOT USE ON LIVE DEPLOYMENTS!</b></font>
</p>
</div>

</div>

<%@ include file='../footer.xml' %>
</body>
</html>
