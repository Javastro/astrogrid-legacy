<%@ page import="org.astrogrid.config.SimpleConfig, javax.naming.*"
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

<form action="../addResourceEntry.jsp" method="post">
<p>
<input type="checkbox" name="validate" value="true">Validate</input>
<input type="hidden" name="addFromText" value="true" />

<textarea name="Resource" cols='60' rows='20'><%@ include file="makeRegistryType.jsp" %></textarea>
<p>
Press this button and if all goes OK then restart the registry service:
<input name="button" value="Register" type="submit">
</form>

</body></html>