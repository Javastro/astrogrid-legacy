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
//   InitialContext jndiContext = new InitialContext();

//   jndiContext.addToEnvironment("org.astrogrid.registry.authorityid", authorityID);
//   jndiContext.addToEnvironment("java:comp/env/org.astrogrid.registry.authorityid", authorityID);
//   jndiContext.bind("org.astrogrid.registry.authorityid", authorityID);
//   jndiContext.bind("java:comp/env/org.astrogrid.registry.authorityid", authorityID);
%>

<p>This is the generated Registry Resource:
<p>

<form action="addResourceEntry.jsp" method="post">
<p>
<input type="checkbox" name="validate" value="true">Validate</input>
<input type="hidden" name="addFromText" value="true" />

<textarea name="Resource" cols='80' rows='20'>
<%
String version = request.getParameter("version");
if(version == null || version.trim().length() <= 0 || version.equals("null") ||  version.equals("0.9")) {
%>
<%@ include file="makeRegistryType0.9.jsp" %>
<%}else if(version.equals("0.10")) { %>
<%@ include file="makeRegistryType0.10.jsp" %>
<%}%>
</textarea>
<p>
Press this button and if all goes OK then restart the registry service:
<input name="button" value="Register" type="submit">
</form>

</body></html>
