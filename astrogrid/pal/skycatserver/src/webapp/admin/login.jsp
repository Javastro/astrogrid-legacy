<%@ page import="org.astrogrid.dataservice.service.DataServer"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title><%=DataServer.getDatacenterName() %> Administration Login</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Administration Login</h1>
<p>
To access the administration pages you will need to login with the role 'paladmin'.
</p>
<form method="POST" action="j_security_check">
   <table>
   <tr><td>User</td><td> <input type="text" name="j_username"></td></tr>
   <tr><td>Password</td><td> <input type="password" name="j_password"></td></tr>
   <tr><td></td><td><input type="submit" value="Log in"></td></tr>
   </table>
</form>
</body>
</html>

