<%@ page import="org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
	             org.apache.commons.fileupload.*,                  
                 java.io.*"
    session="false" %>

<html>
<head><title>Query</title>
</head>

<body>
<h1>Query Registry</h1>

<p>
	You can submit ADQL 0.73 or 0.74 adql queries.<br />
</p>

<%
  Document adql = null;
  boolean isMultipart = FileUpload.isMultipartContent(request);
  if(isMultipart) {
	DiskFileUpload upload = new DiskFileUpload();
	List /* FileItem */ items = upload.parseRequest(request);
	Iterator iter = items.iterator();
	while (iter.hasNext()) {
    	FileItem item = (FileItem) iter.next();
	    if (!item.isFormField()) {
	    	adql = DomHelper.newDocument(item.getInputStream());
	    }//if
	}//while
  }else if(request.getParameter("queryFromURL") != null &&
     request.getParameter("addFromURL").trim().length() > 0) {
     	adql = DomHelper.newDocument(new URL(request.getParameter("docurl")));
  }
  else if(request.getParameter("Resource").trim().length() > 0) {  
  	String resource = request.getParameter("Resource");
  	adql = DomHelper.newDocument(resource);
  }//elseif
%>
<br />
<strong>Only a max of 25 entries will be shown:</strong><br />

<pre>
<%
	   RegistryQueryService server = new RegistryQueryService();
	   Document entry = server.Search(adql);
	   if (entry == null) {
    	  out.write("<p>No entry returned</p>");
	   }
	   else {
      out.write("The xml<br />");
      String testxml = DomHelper.DocumentToString(entry);
      testxml = testxml.replaceAll("<","&lt;");
      testxml = testxml.replaceAll(">","&gt;");
      out.write(testxml);
	   }
%>
</pre>

<a href="registry_index.html">Index</a>

</body>
</html>
