<%@ page import="org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
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
   You may load from a local file, url, or paste the adql in the textbox.<br />
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
<input type="submit" name="button" value="Submit"/>
</p>
</form>

<p>
Here are a few sample adql queries:
</p>
<dl>
<dt>A basic string query for 0.7.3 adql:</dt>
<dd><a href="xqlsample1--adql-v0.7.3.xml">Query for a String</a></dd>
<dt>A basic string query for a LIKE in 0.7.3 adql:</dt>
<dd><a href="xqlsample2--adql-v0.7.3.xml">Query for a String</a></dd>
<dt>A basic string query with an "AND" for 0.7.3 adql:</dt>
<dd><a href="xqlsample3--adql-v0.7.3.xml">Query for a String</a></dd>
<dt>A basic great-than query on SIA long for 0.7.3:</dt>
<dd><a href="xqlsample4--adql-v0.7.3.xml">Greater-Than Query</a></dd>
<dt>A basic string query with an "AND" for 0.7.4 adql:</dt>
<dd><a href="xqlsample1--adql-v0.7.4.xml">Query for a String</a></dd>
</dl>


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
   String resource = request.getParameter("Resource");
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
      
      out.write("<table border=1>");
      out.write("<tr><td>AuthorityID</td><td>ResourceKey</td><td>View XML</td></tr>");
      NodeList identifiers = entry.getElementsByTagNameNS("*","Identifier");
         
      for (int n=0; n < identifiers.getLength();n++) {
         out.write("<tr>\n");
         
         Element resource = (Element) ((Element) identifiers.item(n)).getElementsByTagNameNS("*","ResourceKey").item(0);
         Element authority = (Element) ((Element) identifiers.item(n)).getElementsByTagNameNS("*","AuthorityID").item(0);

         String ivoStr = null;
         if (authority == null) {
            out.write("<td>null?!</td>");
         } else {
            if(authority.getFirstChild() != null) {
               out.write("<td>"+authority.getFirstChild().getNodeValue()+"</td>\n");
               ivoStr = authority.getFirstChild().getNodeValue();
           }//if
         }//else

         if (resource == null) {
            out.write("<td>null?!</td>");
         } else {
            if(resource.getFirstChild() != null) {
               out.write("<td>"+resource.getFirstChild().getNodeValue()+"</td>\n");
               ivoStr += "/" + resource.getFirstChild().getNodeValue();
           }else {
              out.write("<td>null!</td>");
           }
         }//if

         out.write("<td><a href=viewEntryXml.jsp?IVORN="+ivoStr+">View</a></td>\n");
         
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