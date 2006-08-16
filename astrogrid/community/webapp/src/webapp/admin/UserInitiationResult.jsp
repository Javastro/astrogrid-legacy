<%@page session="true"%>

<jsp:useBean class="org.astrogrid.community.webapp.UserAccountBean"
    id="account" scope="page"/>
<%
String userLoginName = request.getParameter("userLoginName");
account.setUserLoginName(userLoginName);
%>

<jsp:useBean class="org.astrogrid.community.webapp.CertificateAuthorityBean" 
    id="ca" scope="session"/>

<!-- Set the parameters of this user into the CA. 
     The values come from the parameters of the call to this page. -->
<jsp:setProperty name="ca" property="userLoginName"/>
<% ca.setUserNewPassword(account.getUserOldPassword()); %>
<% ca.setUserCommonName(account.getUserCommonName()); %>
<html>

<head>
<title>Results of initiation of a community user</title>
<style type="text/css" media="all">
  @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>
<div id="bodyColumn">
<h1>Results of initiation of a community user</h1>
<p>
The certificate authority gave this response to a request to initiate
<jsp:getProperty name="ca" property="userLoginName"/>
(<jsp:getProperty name="ca" property="userCommonName"/>):
</p>
<blockquote>
  <jsp:getProperty name="ca" property="initiationResult"/>
</blockquote>
</body>
</div>

</html>