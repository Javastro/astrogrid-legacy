<%@ page import="org.astrogrid.config.SimpleConfig,
                 org.astrogrid.registry.server.admin.*,
                 org.astrogrid.registry.server.query.*,
                 org.astrogrid.registry.common.RegistryDOMHelper,                 
                 org.astrogrid.registry.server.*,
                 java.util.ArrayList"
   isThreadSafe="false"
   session="false"
%>

<%

      RegistryQueryService server = new RegistryQueryService();
      ArrayList al = server.getAstrogridVersions();
      String version = request.getParameter("version");
	   if(version == null || version.trim().length() <= 0) {
   		version = RegistryDOMHelper.getDefaultVersionNumber();
   	}

      
%>

<html>
<head>
<title>AstroGrid Remove</title>
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
<font color="red">WARNING -- Delete only if you know this particular resource has not been harvested to any other registry.</font><br />
If a resource has already been harvested to other registries, then DO NOT REMOVE the resource just change the status attribute to "deleted".
To remove you must put the full identifier.
</p>

<form method="post">
<p>
Remove Resource:
<select name="version">
   <% for(int k = (al.size()-1);k >= 0;k--) { %>
      <option value="<%=al.get(k)%>"
        <%if(version.equals(al.get(k))) {%> selected='selected' <%}%> 
      ><%=al.get(k)%></option>  
   <%}%>
</select>
 <br />
 <input type="hidden" name="remove_resource" value="true" />
 <input type ="text" name="id" />
 <input type="submit" name="button" value="remove"/>
</p>
</form>

<%
String removeRes = request.getParameter("remove_resource");
String id = request.getParameter("id");
if("true".equals(removeRes)) {
	if(id == null || id.trim().length() == 0) {
	  out.println("<font color'red'>Need to put an id</font>");
	} else {
		if(removeRes != null && removeRes.equals("true")) {
	  		RegistryAdminService ras = new RegistryAdminService();
		  	ras.remove(id,request.getParameter("version"));
	  		out.print("<font color='blue'>Removed resource</font>");
		}
	}
}
%>

</body>
</html>