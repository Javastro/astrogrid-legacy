<%@ page import="org.astrogrid.mySpace.delegate.helper.Assist,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>XML Results Hierarchy</title>
</head>

<body>
<h1>XML Results Hierarchy</h1>

<%
  String[] paramNames={"results"};
  String xmlResults = request.getParameter("results");
%>

<p>
The results from the MySpace service are returned as an XML string.
This page shows a complete, hierarchical representation of this XML
string, though the XML formatting has been removed and the items are
indented for ease of reading.
</p>

<pre>
<%
  Assist assistant = new Assist();
  String displayString = assistant.formatTree(xmlResults);

  out.print(displayString);
%>
</pre>

</body>
</html>
