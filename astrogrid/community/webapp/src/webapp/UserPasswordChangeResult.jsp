<%@page session="true"%>

<jsp:useBean class="org.astrogrid.community.webapp.MyProxyBean" 
    id="myproxy" scope="page"/>

<!-- Set the parameters of this user into the CA. 
     The values come from the parameters of the call to this page. -->
<jsp:setProperty name="myproxy" property="userLoginName"/>
<jsp:setProperty name="myproxy" property="userOldPassword"/>
<jsp:setProperty name="myproxy" property="userNewPassword"/>

<jsp:useBean class="org.astrogrid.community.webapp.UserAccountBean"
    id="account" scope="page"/>
<jsp:setProperty name="account" property="userLoginName"/>
<jsp:setProperty name="account" property="userOldPassword" value=""/>
<jsp:setProperty name="account" property="userOldPassword"/>
<jsp:setProperty name="account" property="userNewPassword"/>

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
<p>
The community database gave this response to a request to change the password:
</p>
<blockquote>
  <jsp:getProperty name="account" property="passwordChangeResult"/>
</blockquote>
<p>
The certificate authority gave this response to a request to change the
password on the user's credentials:
</p>
<blockquote>
  <jsp:getProperty name="myproxy" property="passwordChangeResult"/>
</blockquote>
</body>
</div>

</html>