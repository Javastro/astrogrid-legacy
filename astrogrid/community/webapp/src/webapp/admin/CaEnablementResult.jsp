<%@page session="true"%>

<!-- Create the CA bean if this session doesn't already have one. 
     The password comes from a parameter of the call to this page. -->
<jsp:useBean class="org.astrogrid.community.webapp.CertificateAuthorityBean" 
    id="ca" scope="session"/>
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
This certificate authority issues distinguished names based on
<jsp:getProperty name="ca" property="rootDn"/>.
</p>
</div>

</html>