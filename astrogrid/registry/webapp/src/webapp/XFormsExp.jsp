<%@ page import="org.astrogrid.config.SimpleConfig,
                 org.w3c.dom.NodeList,
                 org.w3c.dom.Node,
                 org.w3c.dom.Element,
                 org.w3c.dom.Document,   
                 org.astrogrid.util.DomHelper,
                 java.io.*,
                 org.astrogrid.registry.server.RegistryServerHelper,
                 javax.xml.transform.*,
					  javax.xml.transform.dom.*,
					  javax.xml.transform.stream.*,
	              org.astrogrid.registry.server.query.*"
   isThreadSafe="false"
   session="false"
%>

<%
String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
String modelDocURL = regBas + "/xforms/model/resource_xforms_model.xml";
String instanceDocURL = regBas + "/xforms/instance/resource_xforms_instance.xml";
ClassLoader loader = this.getClass().getClassLoader();
InputStream is = null;
/*
InputStream  is = loader.getResourceAsStream("xforms/model/resource_xforms_model.xml");
if(is == null) {
  throw new Exception("Could not find xforms model");
}
*/

Document xformsModelDoc = DomHelper.newDocument(new URL((regBas+"/resource_xforms_model.xml"));
/*
is = loader.getResourceAsStream("xforms/instance/authority_xforms_instance.xml");
if(is == null) {
  throw new Exception("Could not find xforms instance");
}
Document xformsInstanceDoc = DomHelper.newDocument(is);
*/

Document xformsInstanceDoc = DomHelper.newDocument(new URL((regBas + "/resource_xforms_instance.xml")));



NodeList instanceNodeList = xformsModelDoc.getElementsByTagNameNS("*","instance");
if(instanceNodeList.getLength() == 0) {
	throw new Exception("Could not find a instance element/node in your xforms model document");
}
Node instanceNode = instanceNodeList.item(0);
instanceNode.appendChild(xformsModelDoc.importNode(xformsInstanceDoc.getDocumentElement(),true));

Source xmlSource = new DOMSource(xformsModelDoc);

/*
is = loader.getResourceAsStream("xforms/xsl/xform.xsl");
if(is == null) {
  throw new Exception("Could not find xform.xsl to perform trasnformation to html");
}
*/

//Source xslSource = new StreamSource(is);
URL urlXformXSL = new URL((regBas + "/xform.xsl"));
Source xslSource = new StreamSource(urlXformXSL.openStream());

StreamResult result = new StreamResult(out);
TransformerFactory transformerFactory = TransformerFactory.newInstance();
Transformer transformer = transformerFactory.newTransformer(xslSource);         
transformer.transform(xmlSource,result);

%>