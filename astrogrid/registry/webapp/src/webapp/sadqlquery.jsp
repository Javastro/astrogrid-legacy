<%@ page import="org.astrogrid.registry.server.query.*,
				 org.astrogrid.registry.server.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.query.sql.Sql2Adql;
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
<title>Edit Registry Entry</title>
<style type="text/css" media="all">
          @import url("style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

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


<form action="adqlquery.jsp">
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
See <a href="reg_xml_samples/queries">For example queries</a>
<%
  if(request.getParameter("performquery") != null && request.getParameter("performquery").trim().equals("true")) {
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
  }else if(request.getParameter("Resource").trim().length() > 0) {  
  
   String resource = Sql2Adql.translateToAdql074("Resource");
   adql = DomHelper.newDocument(resource);
  }//elseif
  String maxCount = SimpleConfig.getSingleton().getString("exist.query.returncount", "25");
%>
<br />
<strong>Only a max of <%= maxCount %> entries will be shown:</strong><br />

<pre>
<%
      RegistryQueryService server = new RegistryQueryService();
      Document entry = server.Search(adql);
      if (entry == null) {
        out.write("<p>No entry returned</p>");
      }
      else {
      String version = null;
      if(entry.getDocumentElement().hasChildNodes()) {
          version = RegistryServerHelper.getRegistryVersionFromNode(entry.getDocumentElement().getFirstChild());
      }else {
          version = RegistryServerHelper.getRegistryVersionFromNode(entry.getDocumentElement());
      }
      
      
      out.write("<table border=1>");
      out.write("<tr><td>AuthorityID</td><td>ResourceKey</td><td>View XML</td></tr>");
      NodeList resources = entry.getElementsByTagNameNS("*","Resource");
         
      for (int n=0; n < resources.getLength();n++) {
         out.write("<tr>\n");
         
//         Element resource = (Element) ((Element) identifiers.item(n)).getElementsByTagNameNS("*","ResourceKey").item(0);
//         Element authority = (Element) ((Element) identifiers.item(n)).getElementsByTagNameNS("*","AuthorityID").item(0);
		String authority = RegistryServerHelper.getAuthorityID((Element)resources.item(n));
		String resource = RegistryServerHelper.getResourceKey((Element)resources.item(n));

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

         out.write("<td><a href=viewResourceEntry.jsp?version="+version+"&IVORN="+ivoStr+">View</a></td>\n");
         
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