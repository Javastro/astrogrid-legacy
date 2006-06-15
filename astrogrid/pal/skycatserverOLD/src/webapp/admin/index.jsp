<%@ page import="org.astrogrid.dataservice.service.DataServer,
  org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title><%=DataServer.getDatacenterName() %> Administration Pages</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Welcome</h1>
<p>
These are the administrator pages for DSA/catalog - the Publisher's Astrogrid Library.
</p>

</body>
</html>

