<%@ page import="org.astrogrid.registry.server.admin.*,
				 org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                  org.apache.axis.utils.XMLUtils,                 
                 java.io.*"
    session="false" %>

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
registration from your registry into the other registry. This form
copies that registration into a specific registry: that on AstroGrid's
machine <span style="font-style: italic;">hydra</span>.<br>
</p>
<input type="hidden" name="postrequest" value="true" />
<p><input name="postregsubmit" value="Set up harvesting by hydra" type="submit"></p>
</form>


<%
  String doRequest = request.getParameter("postrequest");
  if(doRequest != null && doRequest.trim().equals("true")) {
  
	  String resource = request.getParameter("Resource");
	  String postregsubmit = request.getParameter("postregsubmit");
	  String getregsubmit= request.getParameter("getregsubmit");
	  String getregs = request.getParameter("getregs");
	  String fullRegistryAddURL = "http://hydra.star.le.ac.uk:8080/astrogrid-registry/addResourceEntry.jsp";
	  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	  regBas += "getRegistriesXML.jsp";
%>

<h1>Adding Entry</h1>

<p>Service returns:

<pre>
<%
String callURL = fullRegistryAddURL + "?addFromURL=\"true\"&docurl=\"" + regBas + "\"";
out.write("<p>Attempting to tell hydra full registry about you: </p>");
URL url = new URL(callURL);
HttpURLConnection huc = (HttpURLConnection)url.openConnection();
out.write("<p>Connection opened to hydra and hydra is extracting known registry type entries from here, the response code = " 
          + huc.getResponseCode() + "</p>");
%>
</pre>

<% } %>

</body>
</html>