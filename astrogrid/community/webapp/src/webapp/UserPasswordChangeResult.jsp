<%@page session="true"%>

<html>

<head>
<title>Results of password change</title>
<style type="text/css" media="all">
  @import url("style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id="bodyColumn">
<h1>Results of password change</h1>
<p><%=request.getAttribute("org.astrogrid.community.server.verdict")%></p>
</body>
</div>

</html>