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
	<option selected="true" value="0.10">0.10</option>
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
being managed at the Registry. <strong>Community is a little different, if your community identifier is different from your
authority id you use in the registry, then check this box so your community identifier can be registered as a authoirtyid
in the registry.</strong>
</p>

<p>
<input name="button" value="Submit" type="submit">
</p>
</form>
</body></html>
