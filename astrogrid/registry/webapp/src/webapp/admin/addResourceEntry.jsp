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

<html>
<head><title>Add Resource Entry</title>
</head>

<body>

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

<h1>Adding Entry</h1>

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

<a href="registry_index.html">Index</a>

</body>
</html>
