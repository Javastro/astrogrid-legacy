<%@ page import="java.io.File,
                 java.io.FileOutputStream,
                 java.io.InputStream,
                 java.util.Enumeration,
                 javax.xml.parsers.DocumentBuilder,
                 javax.xml.parsers.DocumentBuilderFactory,
                 javax.xml.transform.Transformer,
                 javax.xml.transform.TransformerFactory,
                 javax.xml.transform.dom.DOMSource,
                 javax.xml.transform.stream.StreamResult,
                 org.apache.xpath.XPathAPI,
                 org.w3c.dom.Document,
                 org.w3c.dom.Node"
    session="false" %>
    
<html>

<head>
<title>Properties updated</title>
</head>

<body>

<h1>Properties updated</h1>

<%
// Parse web.xml into a DOM tree.
InputStream  webXmlStream = application.getResourceAsStream("/WEB-INF/web.xml");
DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document webXmlDocument = documentBuilder.parse(webXmlStream);
%>

<table cellpadding="3px">
<tr>
<th>Property name</th>
<th>Result of update</th>
</tr>

<%
// Iterate over the parameters of this JSP. For each, use
// XPath (in the Xalan implemention) to find the env-entry value
// whose name matches the parameter name and then find the the
// text node of the env-entry value (this is all one XPath evaluation).
XPathAPI xpathHelper = new XPathAPI();
Enumeration n = request.getParameterNames();
while (n.hasMoreElements()) {
  String pName = n.nextElement().toString();
  out.println("<tr>");
  try {
    String xpathValue = "//env-entry[env-entry-name='" + pName + "']/env-entry-value";
    Node valueNode = xpathHelper.selectSingleNode(webXmlDocument, xpathValue);
    String xpathText = "text()";
    Node textNode = xpathHelper.selectSingleNode(valueNode, xpathText);
    String[] pValues = request.getParameterValues(pName);
    if (textNode != null) {
      textNode.setNodeValue(pValues[0]);   
    }
    else {
      textNode = webXmlDocument.createTextNode(pValues[0]);
      valueNode.appendChild(textNode);
    }
    out.println("<td>" + pName + "</td><td>set to " + pValues[0] + "</td>");
  }
  catch (Exception e) {
    out.println("<td>" + pName + "</td><td>error:" + e + "</td>");
  }
  out.println("</tr>");
}
%>
</table>

<%
// Use an identity transformation (no XSLT) to
// serialize the DOM tree back into web.xml.
DOMSource webXmlSource = new DOMSource(webXmlDocument);
TransformerFactory factory = TransformerFactory.newInstance();
Transformer transformer = factory.newTransformer();
String path = application.getRealPath("/");
File webXmlFile = new File(path, "WEB-INF/web.xml");
FileOutputStream fos = new FileOutputStream(webXmlFile);
StreamResult webXmlResult = new StreamResult(fos);
transformer.transform(webXmlSource, webXmlResult);
%>

<p>
<a href="viewProperties.jsp">Edit the properties again.</a>
</p>

</body>