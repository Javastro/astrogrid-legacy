<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Setup Pages</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="../admin/navigation.xml" %>

<div id='bodyColumn'>

<h1>Self-registration</h1>

<p>
When a new registry is created, the first registration made in it must describe itself. This form
helps do that.
</p>
<p>

<form action="selfRegisterCheck.jsp" method="post">
<p>
<table>
 <tr><td>Authority ID <td> <input type="text" name="AuthorityID">
 <tr><td>Title        <td> <input type="text" name="Title">
 <tr><td>Publisher    <td> <input type="text" name="Publisher">
 <tr><td>Contact Name <td> <input type="text" name="ContactName">
 <tr><td>Contact email<td> <input type="text" name="ContactEmail">
</table>
<p>
<input name="button" value="Submit" type="submit">
</p>
</form>

</body></html>

