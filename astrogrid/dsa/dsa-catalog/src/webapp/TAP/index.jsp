<%@ page import="org.astrogrid.dataservice.service.DataServer,
                 org.astrogrid.dataservice.service.ServletHelper, 
                 org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title>TAP controls</title>
<style type="text/css" media="all">@import url("../style/astrogrid.css");</style>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="../navigation.xml" %>

<div id='bodyColumn'>

<h1>TAP controls</h1>

<p><a href="TAP-post-query-form.html">Post a new query</a></p>

</div>

</body>
</html>

