<%@ page import="org.astrogrid.registry.server.admin.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,                 
                 java.util.*,
                 org.apache.axis.utils.XMLUtils, 
                 org.apache.commons.fileupload.*, 
                 java.io.*"
    session="false" %>

<%
  Document doc = null;
  boolean update = false;
  boolean isMultipart = FileUpload.isMultipartContent(request);
  System.out.println("the ismultipart = " + isMultipart + " doc url = " + request.getParameter("docurl") + " and addFromURL = " + request.getParameter("addFromURL"));
 if(isMultipart) {
	DiskFileUpload upload = new DiskFileUpload();  
	List /* FileItem */ items = upload.parseRequest(request);
	Iterator iter = items.iterator();
	while (iter.hasNext()) {
    	FileItem item = (FileItem) iter.next();
	    if (!item.isFormField()) {
	    	doc = DomHelper.newDocument(item.getInputStream());
	    }
	}
  	update = true;
  } else if(request.getParameter("addFromURL") != null &&
     request.getParameter("addFromURL").trim().length() > 0) {
     	doc = DomHelper.newDocument(new URL(request.getParameter("docurl")));
     	update = true;
  } else if(request.getParameter("addFromText") != null &&
     request.getParameter("addFromText").trim().length() > 0) {
	 doc = DomHelper.newDocument(request.getParameter("Resource"));
     update = true;
  }
%>

<html>
<head>
<title>Put Resource</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>
<h1>Put Resource</h1>
<p>Inserting/Updating Resource to Registry...
<p>


<p>Service returns:

<pre>
<%
   Document result = null;
   RegistryAdminService server = new RegistryAdminService();
   if(update) {
      result = server.updateResource(doc);
      out.write("<p>Attempt at updating Registry, if any errors occurred it will be printed below<br /></p>");
      if (result != null) {
        DomHelper.DocumentToWriter(result, out);
      }
   }
%>
</pre>

</body>
</html>
