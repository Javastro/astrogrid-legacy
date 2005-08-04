<%@ page import="org.astrogrid.registry.server.query.*,
                 org.w3c.dom.*, javax.xml.transform.*,
                 javax.xml.transform.dom.*,javax.xml.transform.stream.*,                 
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.config.SimpleConfig,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Edit Registry Entry For XForms</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>
<%
   ClassLoader loader = this.getClass().getClassLoader();
   InputStream  is = null;
   String resource = "";
   Document resourceDoc = DomHelper.newDocument();
   Document resourceDocTemp = DomHelper.newDocument();
   BufferedReader br = request.getReader();
   String line = null;
   while((line = br.readLine()) != null) {
      resource += line;
   }
   
   if(resource == null || resource.trim().length() == 0) {
     out.write("No request data sent from the xforms, you can only go to this edit page through xforms, but you may still elect to fill in xml in the text box if desired, but an error most likely occurred. <br />");   
   }
   Document docRead = DomHelper.newDocument(resource);

   Source xmlSource = new DOMSource(docRead);   
   is = loader.getResourceAsStream("xsl/RemoveChildNodes.xsl");
   Source xslSource = new StreamSource(is);
   TransformerFactory transformerFactory = TransformerFactory.newInstance();
          
   DOMResult result = new DOMResult(resourceDoc);
   Transformer transformer = transformerFactory.newTransformer(xslSource);          
   transformer.transform(xmlSource,result);
   
   xmlSource = new DOMSource(resourceDoc);
   result = new DOMResult(resourceDocTemp);
   transformer.transform(xmlSource,result);
   
   xmlSource = new DOMSource(resourceDocTemp);
   resourceDoc = null;
   resourceDoc = DomHelper.newDocument();
   result = new DOMResult(resourceDoc);
   transformer.transform(xmlSource,result);
   
   resource = DomHelper.ElementToString(resourceDoc.getDocumentElement());   
%>

<h1>Add/Update Entry</h1>
<p>
This page shows the xml created from the xforms filled out in the previous page, verify the xml looks correct and hit submit to add/update the registry:
<i>(You may alternatively select the xml in the text box and save it to a file on your local system, for loading later)</i>

<%
if(SimpleConfig.getSingleton().getBoolean("reg.amend.validate",false)) {
%>
<br />
<font color="blue">Validation is already turned on for server side updates.</font><br />
<%
   if(SimpleConfig.getSingleton().getBoolean("reg.amend.quiton.invalid",false)) {
%>
      <font color="blue">It is also set to Quit on invalid updates.</font>
<%}else {%>
      <font color="blue">The server side though will still attempt to update the invalid document.</font>
<%}%>


<%
}else {
%>
<br />
<font color="blue">Validation is <font color="red">Not</font> turned on for server side updates.</font>
<%}%>
</p>

Upload from text:<br />
<form action="addResourceEntry.jsp" method="post">
<!--
<input type="checkbox" name="validate" value="true">Validate</input>
-->
<input type="hidden" name="validate" value="false" />
<input type="hidden" name="addFromText" value="true" />
<p>
<textarea name="Resource" rows="30" cols="90">
<%= resource %>
</textarea>
</p>
<p>
<input type="submit" name="button" value="Submit"/>
</p>
</form>

</body>
</html>