<%@page session="true"%>

<%@include file="beans.xml" %>
<jsp:setProperty name="ca" property="caPassword"/>

<html>

<head>
<title>Results of enabling the certificate authority</title>
<style type="text/css" media="all">
  @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id="bodyColumn">
<h1>Results of enabling the certificate authority</h1>
<p>
<jsp:getProperty name="ca" property="enablementResult"/>
</p>
<p>
This certificate authority issues distinguished names based on
<jsp:getProperty name="ca" property="rootDn"/>.
</p>
</div>

</html>