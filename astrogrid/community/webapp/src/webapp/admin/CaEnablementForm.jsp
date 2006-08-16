<%@page session="true"%>

<html>

<head>
<title>Enabling the certificate authority</title>
<style type="text/css" media="all">
  @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id="bodyColumn">
<h1>Enabling the certificate authority</h1>
<p>
To use the certificate authority, you must enter its passphrase.
The certificate-authority software will retain this passphrase for
your current session only.
</p>
<form action="CaEnablementResult.jsp" method="post">
  <p>
    <label>Passphrase for certificate authority:</label>
    <input type="password" name="caPassword"/>
  </p>
  <p>
    <input type="submit" value="Submit"/>
  </p>
</form>
</div>

</body>

</html>