<%@ page import="org.astrogrid.config.SimpleConfig,
 	  				  org.astrogrid.registry.server.http.servlets.helper.JSPHelper,
                 org.w3c.dom.NodeList,
                 org.w3c.dom.Element,
                 org.w3c.dom.Document,   
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.registry.server.http.servlets.Log4jInit,
                 org.astrogrid.xmldb.client.XMLDBManager,
                 org.astrogrid.registry.common.RegistryDOMHelper,                 
             	  org.astrogrid.registry.server.query.*"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Access Pages</title>
<style type="text/css" media="all">
	<%@ include file="/style/astrogrid.css" %>          
</style>
</title>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<h1>Welcome</h1>
<p>
Welcome to an AstroGrid Registry.  These are the direct access pages for
Registering your resource with the IVO, and/or investigating what resources
are available.
</p>
<p>
<%
   ISearch server = JSPHelper.getQueryService(request);
   
   Document entry = null;
   boolean dbInit = XMLDBManager.isInitialized();
   boolean logInit = Log4jInit.getLoggingInit();
   try {
      entry = server.loadRegistry(DomHelper.newDocument());
   }catch(Exception e) {
      //do nothing for now.  
      entry = null;
   }
   
   boolean newRegistry = true;
   String ivoStr = null;
   if(entry != null) {
      ivoStr = RegistryDOMHelper.getIdentifier(entry.getDocumentElement());
      if(ivoStr != null) {
        newRegistry = false;
      }//if
   }//if
   
   if(!dbInit) {
   	out.write("<font color='red'>Could not detect that your database was initalizaed with the eXist default xml database</font><br />");
   }
   if(!logInit) {
   	out.write("<font color='red'>Could not initalize logging, See <a href='docs/configure.html'>Configure</a></font><br />");
   }
   
   if (newRegistry) {
      out.write("This Registry has not yet been configured; click <a href='docs/configure.html'>here</a> to set it up");
   }
   else {
      out.write("This Registry main authorityid <b>"+SimpleConfig.getSingleton().getString("reg.amend.authorityid")+"</b>");
      out.write("<br />Click <a href='viewResourceEntry.jsp?IVORN=" + ivoStr + "'>here</a> to see the main Registry type for this registry and all authority ids managed by this registry.");
      if("xmldb:exist://".equals(SimpleConfig.getSingleton().getString("xmldb.uri")) &&
         (SimpleConfig.getSingleton().getString("reg.custom.exist.configuration",null) == null ||
          SimpleConfig.getSingleton().getString("reg.custom.exist.configuration").trim().length() == 0)) {
          out.write("<br /><font color='green'>Your registry is in embed/internal mode with the data storage inside your webapp. It is advisable" +
          " to have the data storage outside your webapp, read the configure page to see how this is done in the documentation area.</font>");
      }
   }
%>
	<br /><br /><br />
	  <i>If this registry is going to be a 'Full' Registry and is expected to have the Astrogrid workbench connected to it, please read the
	  <a href='docs\upgrading.html'>upgrading</a> documentation</i>
</body>
</html>