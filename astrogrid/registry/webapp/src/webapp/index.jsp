<%@ page import="org.astrogrid.config.SimpleConfig,
                 org.w3c.dom.NodeList,
                 org.w3c.dom.Element,
                 org.w3c.dom.Document,   
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.registry.server.RegistryServerHelper,                 
             org.astrogrid.registry.server.query.*"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Access Pages</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Welcome</h1>
<p>
Welcome to an AstroGrid Registry.  These are the direct access pages for
Registering your resource with the IVO, and/or investigating what resources
are available.
</p>
<p>
<%
   RegistryQueryService server = new RegistryQueryService();
   Document entry = null;
   try {
      entry = server.loadRegistry(DomHelper.newDocument());
   }catch(Exception e) {
      //do nothing for now.  
   }
   boolean newRegistry = true;
   String ivoStr = null;
   if(entry != null) {
      ivoStr = RegistryServerHelper.getIdentifier(entry.getDocumentElement());
      if(ivoStr != null) {
        newRegistry = false;
      }//if
   }//if
   
   if (newRegistry) {
      out.write("This Registry has not yet been configured; click <a href='setup/install.jsp'>here</a> to set it up");
   }
   else {
      out.write("This Registry main authorityid <b>"+SimpleConfig.getSingleton().getString("reg.amend.authorityid")+"</b>");
      out.write("<br />Click <a href='viewResourceEntry.jsp?IVORN=" + ivoStr + "'>here</a> to see the main Registry type for this registry and all authority ids managed by this registry.");
   }
%>
</body>
</html>