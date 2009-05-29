<%@ page import="org.astrogrid.config.SimpleConfig, javax.naming.*"
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

<%
   String authorityID = request.getParameter("AuthorityID");
%>

<p>This is the generated Registry Resource and the corresponding types Authority and Organisation:
<i>Created and Updated date attributes on the Resources are only here for validation, they will be
changed correctly when updating.</i>
<p>

<form action="addResourceEntry.jsp" method="post">
<p>
<input type="checkbox" name="validate" value="true">Validate</input>
<input type="hidden" name="addFromText" value="true" />

<textarea name="Resource" cols='80' rows='20'>
<%
String version = request.getParameter("version");
if(version.equals("1.0")) { %>
<ri:VOResources
          xmlns:ri="http://www.ivoa.net/xml/RegistryInterface/v1.0"
          xmlns:vr="http://www.ivoa.net/xml/VOResource/v1.0"
          xmlns:vg="http://www.ivoa.net/xml/VORegistry/v1.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" from="1" numberReturned="3" more="false">
<%@ include file="makeRegistryType1.0.jsp" %>
<%@ include file="makeOrganisationType1.0.jsp" %>
<%@ include file="makeAuthorityType1.0.jsp" %>
</ri:VOResources>
<%} %>
</textarea>
<p>
<input name="button" value="Register" type="submit">
</form>

</body></html>
