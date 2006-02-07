<%@ page import="org.astrogrid.registry.server.query.*,
                 org.astrogrid.registry.client.query.*,
                 org.astrogrid.registry.client.admin.*,
 	  				  org.astrogrid.registry.server.http.servlets.helper.JSPHelper,
                 org.astrogrid.registry.client.*,
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

<html>
<head>
<title>Adding External Registries</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>
<%
  String regAddurl = request.getParameter("regaddurl");
  if(regAddurl == null || regAddurl.trim().length() == 0) {
  	regAddurl = "http://galahad.star.le.ac.uk:8081/astrogrid-registry";
  }
%>
<form action="registerSelfInExternalRegistry.jsp" method="post">
  <p>In order to make another registry to mirror ("harvest") the contents of your registry, you must copy the <span style="font-style: italic;">registryType</span>
registration from your registry into the other registry. This form
copies that registration into a specific registry: that on AstroGrid's
machine <span style="font-style: italic;">Galahad</span>.<br>
</p>
<input type="hidden" name="postrequest" value="true" />
<input name="regaddurl" type="text" value=<%=regAddurl%>   size="100" />
<p><input name="postregsubmit" value="Set up harvesting" type="submit"></p>
</form>


<%
  String doRequest = request.getParameter("postrequest");

  if(doRequest != null && doRequest.trim().equals("true")) {  
%>

<h1>Adding Entry</h1>

<p>Service returns:

<pre>
<%

RegistryAdminService ras = RegistryDelegateFactory.createAdmin(new URL(regAddurl + "/services/RegistryUpdate"));
ISearch server = JSPHelper.getQueryService(request);
Document regDoc = server.getQueryHelper().loadMainRegistry();
String regDocString = DomHelper.DocumentToString(regDoc);
System.out.println("okay here is what should be sent = " + 
regDocString.substring(regDocString.indexOf(">",regDocString.indexOf("SearchResponse"))+1,regDocString.indexOf("</SearchResponse")).trim());
Document resultDoc = ras.updateFromString(regDocString.substring(regDocString.indexOf(">",regDocString.indexOf("SearchResponse"))+1,regDocString.indexOf("</SearchResponse")).trim());
System.out.println("the resultDoc = " + DomHelper.DocumentToString(resultDoc));
%>

</pre>

<% } %>
</body>
</html>