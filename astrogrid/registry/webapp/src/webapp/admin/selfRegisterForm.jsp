<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Setup Pages</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</title>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<h1>Self-registration</h1>

<p>
When a new registry is created, the first registration made in it must describe itself. This form
helps do that. Optionally you may go back and submit your own registry type resource entry by clicking on "Enter Resource" on the menu.
But this is a quicker way to fill in the majority of known values that change.
Currently 0.10 is the main VOResource, but you may wish to come back to this screen and do 1.0.  This release is one of the 
first implemenations of 1.0; other registries do not yet support it, but will soon.
</p>
<p>
<%
String authID = SimpleConfig.getSingleton().getString("reg.amend.authorityid", "");
%>

<form action="selfRegisterCheck.jsp" method="post">
<p>
<input type="hidden" name="version" value="<%=org.astrogrid.registry.server.http.servlets.helper.JSPHelper.getQueryService(request).getResourceVersion()%>" />
<input type="hidden" name="AuthorityID" value="<%=authID%>" />
<table>
 <tr><td>Authority ID </td><td> <%=authID%></td></tr>
 <tr><td>Title        </td><td> <input type="text" name="Title"></td></tr>
 <tr><td>Publisher    </td><td> <input type="text" name="Publisher"></td></tr>
 <tr><td>Contact Name </td><td> <input type="text" name="ContactName"></td></tr>
 <tr><td>Contact email</td><td> <input type="text" name="ContactEmail"></td></tr>
 <tr><td>Full Registry (1.0 only)</td><td><select name="fullregistry"><option value="false">No</option><option value="true">Yes</option></select></td></tr>
  <tr><td>Description</td><td> <input type="text" name="ContentDescription"></td></tr>
  <tr><td>Reference URL:</td><td> <input type="text" name="ContentRefURL"></td></tr>
  <tr><td>Facility Name</td><td> <input type="text" name="Facility"></td></tr>
</table>
<p>
<input name="button" value="Submit" type="submit">
</p>
</form>
</body></html>