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
helps do that. Optionally you may go back and submit your own registry type resource entry by clicking on "Enter Resource" on the menu.
This is a quicker way to fill in the majority of known values that change.
</p>
<p>

<form action="selfRegisterCheck.jsp" method="post">
<p>
<input type="text" name="version" value="0.10" />
<table>
 <tr><td>Authority ID <td> <input type="text" name="AuthorityID"  size="30" value="<%= request.getParameter("auth") %>">
 <tr><td>Title        <td> <input type="text" name="Title"        size="30" value="<%= request.getParameter("title") %>">
 <tr><td>Publisher    <td> <input type="text" name="Publisher"    size="30" value="<%= request.getParameter("pub") %>">
 <tr><td>Contact Name <td> <input type="text" name="ContactName"  size="30">
 <tr><td>Contact email<td> <input type="text" name="ContactEmail" size="30">
</table>
<p>
<input name="button" value="Submit" type="submit">
</p>
</form>
</body></html>
