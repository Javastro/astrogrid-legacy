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
<head><title>Add Resource Entry</title>
</head>

<body>

<%
  String resource = request.getParameter("Resource");
  String postregsubmit = request.getParameter("postregsubmit");
  String getregsubmit= request.getParameter("getregsubmit");
  String getregs = request.getParameter("getregs");
  String fullRegistryAddURL = "http://hydra.star.le.ac.uk:8080/astrogrid-registry/addResourceEntry.jsp";
  //String regBas = request.getRequestURL().toString();
  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
%>

<h1>Adding Entry</h1>

<p>Service returns:

<pre>
<%
String callURL = fullRegistryAddURL + "?getregssubmit=\"true\"&getregs=\"" + regBas + "\"";
out.write("<p>Attempting to tell hydra full registry about you: </p>");
URL url = new URL(callURL);
HttpURLConnection huc = (HttpURLConnection)url.openConnection();
out.write("<p>Connection opened to hydra and hydra is extracting known registry type entries from here, the response code = " 
          + huc.getResponseCode() + "</p>");
%>
</pre>

<a href="index.html">Return to set-up index</a>

</body>
</html>
