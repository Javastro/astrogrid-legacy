<%@ page import="org.astrogrid.registry.server.admin.*,
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
<title>Set up harvesting of another registry</title>
</head>

<body>

<%
  String postregsubmit = request.getParameter("postregsubmit");
  String getregsubmit= request.getParameter("getregsubmit");
  String getregs = request.getParameter("getregs");
  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>

<h1>Adding Entry</h1>

<p>Service returns:

<pre>
<%
RegistryAdminService server = new RegistryAdminService();
String domurl = getregs + "/getRegistries.jsp";
URL urlDom = new URL(domurl);
System.out.println("the domurl = " + domurl);
out.write("<p>getregs: " + getregs + "</p><br />");
out.write("<p>url to grab registries : " + domurl + "</p><br />");
Document doc = DomHelper.newDocument(urlDom);
Document result = server.updateResource(doc);
out.write("<p>Attempt at grabbing registries from above url and updating the registry, any errors in the updating of this registry will be below<br /></p>");
if (result != null) {
  XMLUtils.ElementToWriter(result.getDocumentElement(), out);
}
out.write("<p><br /><br />Here were the entries attempted to be updated into the registry (Remember only the Resource elements are placed into the registry):<br /></p>");
if(doc != null) {
  XMLUtils.ElementToWriter(doc.getDocumentElement(), out);       
}
   
%>
</pre>

<a href="index.html">Index</a>

</body>
</html>
