<%@ page import="org.astrogrid.config.SimpleConfig,
                 org.astrogrid.registry.server.admin.*,
                 org.astrogrid.registry.server.query.*,
                 org.astrogrid.registry.server.*,
                 java.util.ArrayList"
   isThreadSafe="false"
   session="false"
%>

<%
/*
      RegistryQueryService server = new RegistryQueryService();
      ArrayList al = server.getAstrogridVersions();
      String version = request.getParameter("version");
	   if(version == null || version.trim().length() <= 0) {
   		version = RegistryServerHelper.getDefaultVersionNumber();
   	}
*/
      
%>

<html>
<head>
<title>AstroGrid Registry Access Pages</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Clear Managed Authorities</h1>

<p>
Allows you to clear managed authorities, in which the next update will refreash all the managed authorities.
</p>

<form method="post">
<p>
Clear Managed Authroities for Version:
 <br />
 <input type="hidden" name="clear_cache" value="true" />
 <input type="submit" name="button" value="clear cache"/>
</p>
</form>

<%
String clearCache = null;
clearCache = request.getParameter("clear_cache");
if(clearCache != null && clearCache.equals("true")) {
  	RegistryAdminService ras = new RegistryAdminService();
  	ras.clearManagedCache();
  	out.print("<font color='blue'>Cleared cache for " + version + "</font>");
}
%>

</body>
</html>

