<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Access Pages</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Welcome</h1>
<p>
Welcome to an AstroGrid Registry.  These are the direct access pages for
Registering your resource with the IVO, and/or investigating what resources
are available.
</p>
<p>
<%
   if (SimpleConfig.getSingleton().getString("org.astrogrid.registry.authorityid", null) == null) {
      out.write("This Registry has not yet been configured; click <a href='setup/install.jsp'>here</a> to set it up");
   }
   else {
      out.write("This Registry manages the authority <b>"+SimpleConfig.getSingleton().getString("org.astrogrid.registry.authorityid")+"</b>");
   }
%>

</body>
</html>

