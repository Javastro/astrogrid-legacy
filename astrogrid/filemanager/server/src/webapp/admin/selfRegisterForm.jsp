<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>
<html>
    <head>
        <title>Service registration</title>
        <style type="text/css" media="all">
            @import url("../style/astrogrid.css");
        </style>
    </head>
<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Registration</h1>

<p>
When setting up file manager for the first time, you need to register your resources to a registry.  Fill in the 
details below and hit submit. 
Be sure to have set your Registry endpoints in the JNDI Administration GUI. 
Your Publishing registry should be at a location that will or currently has your AuthorityID.  An added ability has
been added for submitting a Authority Resource entry for a registry to manage your AuthorityID if needed.
</p>
<p>

<form action="selfRegisterCheck.jsp" method="post">
<p>
<select name="version">
	<option value="0.9">0.9</option>
	<option value="0.10" selected="selected">0.10</option>
</select>
<table>
 <tr><td>Contact Name <td> <input type="text" name="ContactName">
 <tr><td>Contact Email        <td> <input type="text" name="ContactEmail">
</table>
<p>
<input type="checkbox" name="AuthorityResourceAdd"/>Add Authority Resource
<br />
The checkbox above is when you want to register a brand new Authority id to a registry to be managed. 
You only need to do this the first time if ever.  In general you should not do this and should choose a Authority id that is already
being managed at the Registry.
</p>

<p>
<input name="button" value="Submit" type="submit">
</p>
</form>
</body></html>
