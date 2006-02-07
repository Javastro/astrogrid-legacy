<%@ page import="org.astrogrid.registry.server.query.*,
				     org.astrogrid.registry.server.*,
				     org.astrogrid.registry.common.RegistryDOMHelper,
 	  				  org.astrogrid.registry.server.http.servlets.helper.JSPHelper, 
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.query.sql.Sql2Adql,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.config.SimpleConfig,
                 org.w3c.dom.NodeList,
                 org.w3c.dom.Element,                 
                 java.net.*,
                 java.util.*,
                 org.apache.commons.fileupload.*,                  
                 java.io.*"
    session="false" %>

<html>
<head>
<title>Advanced Query of Registry</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<%

%>

<h1>Query Registry</h1>

<p>
   You can submit ADQL 0.73 or 0.74 adql queries. 
   The first two options are for loading adql (xml version) from a local file or url.
   The third option allows you to put in sql statements in which then we will translate it to
   adql for querying the registry.
   <br />
   Results will be shown below.<br />
</p>

<form enctype="multipart/form-data" method="post">
<input type="file" name="docfile" />
<input type="hidden" name="addFromFile" value="true" />
<input type="hidden" name="performquery" value="true" />
<input type="submit" name="uploadFromFile" value="upload" />
</form>

<br />
<form method="post">
<input type="text" name="docurl" />
<input type="hidden" name="performquery" value="true" />
<input type="hidden" name="queryFromURL" value="true" />
<input type="submit" name="uploadFromURL" value="upload" />
</form>


<form action="sadqlquery.jsp">
<p>
<input type="hidden" name="performquery" value="true" />
<textarea name="Resource" rows="30" cols="90"></textarea>
</p>
<p>
<input type="submit" name="button" value="Submit"/><br />
Example SQL:<br />
Select * from Registry where vr:title = 'Astrogrid' and vr:content/vr:description like '%scope%'
</p>
</form>

<%
  boolean isMultipart = FileUpload.isMultipartContent(request);
  if(isMultipart || (request.getParameter("performquery") != null && request.getParameter("performquery").trim().equals("true"))) {
	  ISearch server = JSPHelper.getQueryService(request);
  	  Document adql = null;
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
	     request.getParameter("queryFromURL").trim().length() > 0) {
	     adql = DomHelper.newDocument(new URL(request.getParameter("docurl")));
	  }else if(request.getParameter("Resource").trim().length() > 0) {  
	  System.out.println("okay lets do the translation");
	   String resource = Sql2Adql.translateToAdql074(request.getParameter("Resource").trim());
	   System.out.println("translated to = " + resource);
	   adql = DomHelper.newDocument(resource);
	  }//elseif
  String maxCount = SimpleConfig.getSingleton().getString("exist.query.returncount", "25");
%>
<br />
<strong>Only a max of <%= maxCount %> entries will be shown:</strong><br />

<pre>
<%

      Document entry = server.Search(adql);
      if (entry == null) {
        out.write("<p>No entry returned</p>");
      }
      else {      
      
      out.write("<table border=1>");
      out.write("<tr><td>AuthorityID</td><td>ResourceKey</td><td>Actions</td></tr>");
      NodeList resources = entry.getElementsByTagNameNS("*","Resource");
         
      for (int n=0; n < resources.getLength();n++) {
         out.write("<tr>\n");
         
		String authority = RegistryDOMHelper.getAuthorityID((Element)resources.item(n));
		String resource = RegistryDOMHelper.getResourceKey((Element)resources.item(n));

         String ivoStr = null;
         if (authority == null || authority.trim().length() <= 0) {
            out.write("<td>null?!</td>");
         } else {
               out.write("<td>"+authority+"</td>\n");
               ivoStr = authority;
         }//else

         if (resource == null || resource.trim().length() <= 0) {
            out.write("<td>null?!</td>");
         } else {
               out.write("<td>"+resource+"</td>\n");
              ivoStr += "/" + resource;
         }//if
         ivoStr = java.net.URLEncoder.encode(("ivo://" + ivoStr),"UTF-8");         
         
         String xsiType = ((Element)resources.item(n)).getAttribute("xsi:type");
         if(xsiType.indexOf(":") != -1) {
           xsiType = xsiType.substring(xsiType.indexOf(":")+1);
         }
         
         out.write("<td><a href=viewResourceEntry.jsp?IVORN="+ivoStr+">View,</a>");
         out.write("<a href=admin/editEntry.jsp?IVORN="+ivoStr+">Edit,</a>");
         out.write("<a href=admin/xforms/XFormsProcessor.jsp?mapType="+xsiType+"&IVORN="+ ivoStr + ">XEdit</a></td>");
                  
         out.write("</tr>\n");         
      }                  
         out.write("</table> <hr />");      
         out.write("The xml<br />");
        String testxml = DomHelper.DocumentToString(entry);
         testxml = testxml.replaceAll("<","&lt;");
        testxml = testxml.replaceAll(">","&gt;");
         out.write(testxml);
      }//else
      
%>
</pre>

<% } %>

</body>
</html>