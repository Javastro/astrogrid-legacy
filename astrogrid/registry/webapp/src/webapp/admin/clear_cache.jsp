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
   		version = RegistryDOMHelper.getDefaultVersionNumber();
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
<font color="red">Deprecated - prefer not to use, most likely your Registry types are not set right if your
getting errors on managed authorities.</font><br />
Allows you to clear managed authorities, in which the next update will refreash all the managed authorities. 
Normally this is not needed, but if your getting results that you can't update because it is not managed by
this registry and your certain that it should be, you may clear it.  The managedAuthority
elements in Registry types are what is read and they should not be duplicated in any other Registry Type.
</p>

<form method="post">
<p>
Clear Managed Authorities:
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
  	out.print("<font color='blue'>Cleared cache</font>");
}
%>

</body>
</html>

