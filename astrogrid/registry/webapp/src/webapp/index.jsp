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
      entry = DomHelper.newDocument(server.getQueryHelper().loadMainRegistry().getMembersAsResource().getContent().toString());
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
   }
%>
	<br />
	<p>
	  <i>This is a new Registry and is in a transition state to 1.0 type Resources.  By default this registry is set to 1.0 Resource (and Contract).  But can handle multiple versions
	  including the older 0.10 Resources.  Before 1.0 there was a 0.10 version of the Resource schema but the query&harvest interface specification was much further behind and its version was 0.1 a.k.a 0.1 contract version.
	  As noted this registry can handle both (0.10 and 1.0 Resource versions) hence (0.1 and 1.0 contract versions).  Quite simply 0.1,0.10 go together and 1.0,1.0 go together.
	  There are a few new features to this registry and its jsp pages to help you manage the registry.  
	  <b>Note the 'Current Contracts' in the menu</b>  this will allow you to switch between different Resource versions in case you need to query, update or manage the other version of Resources.  By default this is
	  only set for the session of your browser.  So be sure you are on the correct version.
	</p>
</body>
</html>