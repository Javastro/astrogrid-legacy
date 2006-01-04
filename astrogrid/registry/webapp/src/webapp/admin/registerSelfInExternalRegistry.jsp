<%@ page import="org.astrogrid.registry.server.admin.*,
				     org.astrogrid.registry.server.query.*,
				     org.astrogrid.registry.server.*,
                 org.astrogrid.store.Ivorn,
                 org.astrogrid.registry.common.RegistryDOMHelper,
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

<form action="registerSelfInExternalRegistry.jsp" method="post">
  <p>In order to make another registry to mirror ("harvest") the contents of your registry, you must copy the <span style="font-style: italic;">registryType</span>
registration from your registry into the other registry.
</p>
<input type="hidden" name="postrequest" value="true" />
<select name="version">
   <% for(int k = (al.size()-1);k >= 0;k--) { %>
      <option value="<%=al.get(k)%>"
        <%if(version.equals(al.get(k))) {%> selected='selected' <%}%> 
      ><%=al.get(k)%></option>  
   <%}%>
</select>
<%
    String prevurl = request.getParameter("regaddurl");
    if (null == prevurl)
        {
        prevurl = "target registry url" ;
        }
%>
<input name="regaddurl" size="40" type="text" value="<%= prevurl %>"/>
<p><input name="postregsubmit" value="Set up harvesting" type="submit"></p>
</form>


<%
  String doRequest = request.getParameter("postrequest");
  if(doRequest != null && doRequest.trim().equals("true")) {
  
	  String resource = request.getParameter("Resource");
	  String postregsubmit = request.getParameter("postregsubmit");
	  String getregsubmit= request.getParameter("getregsubmit");
	  String getregs = request.getParameter("getregs");
	  String regaddurl = request.getParameter("regaddurl");
	  String fullRegistryAddURL = regaddurl + "/addResourceEntry.jsp";
	  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
      regBas += "/getRegistriesXML.jsp?version=" + version ;
%>

<h1>Adding Entry</h1>

<p>Service returns:

<pre>
<%
String callURL = fullRegistryAddURL + "?addFromURL=true&docurl=" + regBas ;
out.write("<p>Calling remote registry to initiate harvest from this registry</p>");
URL url = new URL(callURL);
HttpURLConnection huc = (HttpURLConnection)url.openConnection();
out.write("<p>Connection opened to remote registry .... the response code = " 
+ huc.getResponseCode() + "</p>");
%>
</pre>

<% } %>

</body>
</html>
