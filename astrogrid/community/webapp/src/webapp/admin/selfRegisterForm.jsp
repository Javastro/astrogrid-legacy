<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Community publish to Registry</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Registration</h1>

<p>
When setting up community for the first time, you need to register your resources to a registry.  Fill in the 
details below and hit submit.
</p>
<p>

<form action="selfRegisterCheck.jsp" method="post">
<p>
<select name="version">
	<option value="0.9">0.9</option>
	<option value="0.10">0.10</option>
</select>
<table>
 <tr><td>Contact Name <td> <input type="text" name="ContactName">
 <tr><td>Contact Email        <td> <input type="text" name="ContactEmail">
</table>
<p>
<input name="button" value="Submit" type="submit">
</p>
</form>
</body></html>