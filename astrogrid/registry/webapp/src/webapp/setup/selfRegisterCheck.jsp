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
   InitialContext jndiContext = new InitialContext();

   jndiContext.addToEnvironment("org.astrogrid.registry.authorityid", authorityID);
   jndiContext.addToEnvironment("java:comp/env/org.astrogrid.registry.authorityid", authorityID);
//   jndiContext.bind("org.astrogrid.registry.authorityid", authorityID);
//   jndiContext.bind("java:comp/env/org.astrogrid.registry.authorityid", authorityID);
%>
<p>The property <code>org.astrogrid.registry.authorityid</code> has been set
to <code><%= authorityID %></code> <i><b>No it hasn't there's some problem with this</b>.

<p>This is the generated Registry Resource:
<p>

<form action="../admin/putResource.jsp" method="post">
<p>

<textarea name="Resource" cols='60' rows='20'><%@ include file="makeRegistryType.jsp" %></textarea>
<p>
Press this button and if all goes OK then restart the registry service:
<input name="button" value="Register" type="submit">
</form>

</body></html>

