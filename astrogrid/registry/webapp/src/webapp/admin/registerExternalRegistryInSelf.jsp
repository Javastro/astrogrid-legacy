<%@ page import="org.astrogrid.registry.server.admin.*,
 				     org.astrogrid.registry.server.query.*,
 				     org.astrogrid.registry.server.*,
 				     org.astrogrid.registry.common.RegistryDOMHelper,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                  org.apache.axis.utils.XMLUtils,                 
                 java.io.*"
    session="false" %>
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
<title>Adding External Registries</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<p>To mirror ("harvest") the contents of other registries, you must
collect their registryType entries and add those entries to your own
registry. The form below causes your registry to copy these entries
from a specific, external registry. Note that harvesting another
registry adds its contents to the contents of your registry; nothing in
your registry is lost or overwritten.<br>

</p>

<form method="post">
<p>
Please enter the URL for a registry to be harvested.
A default registry in AstroGrid is pre-set.
</p>
<p>
<select name="version">
   <% for(int k = (al.size()-1);k >= 0;k--) { %>
      <option value="<%=al.get(k)%>"
        <%if(version.equals(al.get(k))) {%> selected='selected' <%}%> 
      ><%=al.get(k)%></option>  
   <%}%>
</select>
<input name="getregs" value="http://hydra.star.le.ac.uk:8080/astrogrid-registry" size="64" type="text">
<input name="getregsubmit" value="Set up harvesting" type="submit">
</p>
</form>

<%
  String postregsubmit = request.getParameter("postregsubmit");
  String getregsubmit= request.getParameter("getregsubmit");
  String getregs = request.getParameter("getregs");
  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
  if(getregs != null && getregs.trim().length() > 0) {
%>

<h1>Grabbing and Adding Registry Entries From <%= getregs %></h1>

<p>Service returns:

<pre>
<%
RegistryAdminService serverAdmin = new RegistryAdminService();
String domurl = getregs + "/getRegistriesXML.jsp?version=" + version;
URL urlDom = new URL(domurl);
//System.out.println("the domurl = " + domurl);
out.write("<p>getregs: " + getregs + "</p><br />");
out.write("<p>url to grab registries : " + domurl + "</p><br />");
Document doc = DomHelper.newDocument(urlDom);
//Document result = serverAdmin.updateResource(doc);
serverAdmin.updateNoCheck(doc,version);

out.write("<p>Attempt at grabbing registries from above url and updating the registry, any errors in the updating of this registry will be below<br /></p>");
//if (result != null) {
//  XMLUtils.ElementToWriter(result.getDocumentElement(), out);
//}
out.write("<p><br /><br />Here were the entries attempted to be updated into the registry (Remember only the Resource elements are placed into the registry):<br /></p>");
//System.out.println("result not null");
if(doc != null) {
	//System.out.println("doc not null");
      String testxml = DomHelper.DocumentToString(doc);
      testxml = testxml.replaceAll("<","&lt;");
      testxml = testxml.replaceAll(">","&gt;");
      out.write(testxml);
}
   
%>
</pre>

<%}%>

</body>
</html>
