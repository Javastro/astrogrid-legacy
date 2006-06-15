<%@ page import="org.astrogrid.dataservice.service.DataServer,
  org.astrogrid.cfg.ConfigFactory"
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
This page is only accessible to DSA/catalog administrators.
</p>
<p>
Please log in using your administrator username and password.
</p>


<form method="POST" action="j_security_check">
   <table>
   <tr><td>Username</td><td> <input type="text" name="j_username"></td></tr>
   <tr><td>Password</td><td> <input type="password" name="j_password"></td></tr>
   <tr><td></td><td><input type="submit" value="Log in"></td></tr>
   </table>
</form>

<p>
If you're uncertain about configuring the administrator username and 
password,<br/>
please see <a href="../docs/configure_step1.jsp">Step 1: Check your tomcat user configuration</a> in the <a href="../configure.jsp">Configuration documentation.</a>
</p>

</body>
</html>

