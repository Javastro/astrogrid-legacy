<%@ page import="org.astrogrid.registry.server.query.*,
                 org.astrogrid.registry.client.query.*,
                 org.astrogrid.registry.client.*,
                 org.astrogrid.registry.server.*,
                 org.astrogrid.registry.common.RegistryDOMHelper,
                 org.astrogrid.registry.common.RegistryValidator,             
                 junit.framework.AssertionFailedError,
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
<title>Advanced Query of Registry for any Registry</title>
<style type="text/css" media="all">
          @import url("style/ivoa.css");
</style>
</head>

<body>
<%@ include file="ivoa_header.xml" %>
<%@ include file="ivoa_navigation.xml" %>

<div id='bodyColumn'>

<h1>Query Registry</h1>

<p>
   The first two options are for loading adql (xml version) from a local file or url.
   The third option allows you to put in sql statements in which then we will translate it to
   adql for querying the registry.
   <br />
   Results will be shown below.<br />
</p>

<form enctype="multipart/form-data" method="post">
<p>
Upload adql xml file:<br />
<br />Endpoint: <input type="text"   size="100" name="endpoint" value="<%= request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath() %>/services/RegistryQuery" /><br />
<input type="file"   size="100"  name="docfile" />
<input type="hidden" name="addFromFile" value="true" />
<input type="hidden" name="performquery" value="true" /><br />
<input type="submit" name="uploadFromFile" value="upload" />
</p>
</form>

<br />
<form method="post">
<p>
URL to an adql xml file: <br />
<br />Endpoint: <input type="text"  size="100" name="endpoint" value="<%= request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath() %>/services/RegistryQuery" /><br />
<input type="text"  size="100" name="docurl" />
<input type="hidden" name="performquery" value="true" />
<input type="hidden" name="queryFromURL" value="true" /><br />
<input type="submit" name="uploadFromURL" value="upload" />
</p>
</form>

<form method="post">
<p>
Paste ADQL xml version:<br />
<br />Endpoint: <input type="text"   size="100"  name="endpoint" value="<%= request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath() %>/services/RegistryQuery" /><br />
<input type="hidden" name="performquery" value="true" />
<input type="hidden" name="xadql" value="true" />
<textarea name="Resource" rows="30" cols="90"></textarea>
<br />
<input type="submit" name="button" value="Submit"/><br />
</p>

</form>


<form method="post">
<p>
Enter SQL (actually adqls).  This will only send adql 0.7.4 at the moment use above approaches to send other versions:<br />
<br />Endpoint: <input type="text"   size="100"  name="endpoint" value="<%= request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath() %>/services/RegistryQuery" /><br />
<input type="hidden" name="performquery" value="true" />
<i>Only for ADQL 0.7.4</i><br />
<textarea name="Resource" rows="30" cols="90"></textarea>
</p>
<p>
<input type="submit" name="button" value="Submit"/><br />
Example SQL:<br />
Select * from Registry where vr:title = 'hello' and vr:content/vr:description like '%world%'
</p>
</form>

<%


  if(request.getParameter("performquery") != null && request.getParameter("performquery").trim().equals("true")) {
   System.out.println("performQuery true and endpoint = " + request.getParameter("endpoint"));
   if(request.getParameter("endpoint") != null && request.getParameter("endpoint").trim().length() > 0) {
   
   String endpoint = request.getParameter("endpoint").trim();
  Document adql = null;
  boolean isMultipart = FileUpload.isMultipartContent(request);
  System.out.println("The-get2 Resource = " + request.getParameter("Resource") + " and size of it = " + request.getParameter("Resource").trim().length());
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
  }else if(request.getParameter("xadql") != null && 
           request.getParameter("Resource").trim().length() > 0) {
     adql = DomHelper.newDocument(request.getParameter("Resource").trim());                      
  } else if(request.getParameter("Resource").trim().length() > 0) {  
  System.out.println("okay lets do the translation");
   String resource = Sql2Adql.translateToAdql074(request.getParameter("Resource").trim());
   System.out.println("okay about to do vrNS");
   adql = DomHelper.newDocument(resource);
  }//elseif
  
%>
<br />

<pre>
<%

      RegistryService rs = RegistryDelegateFactory.createQuery(new URL(endpoint));
      Document entry = rs.search(adql);
      out.write("<p>If entries are returned, then the xml will be validated, shown tabular, then full xml at the bottom.");
      if (entry == null) {
        out.write("<p>No entry returned</p>");
      }
      else {
      if(entry.getElementsByTagNameNS("*","SearchResponse").getLength() == 0)
         out.write("<p><font color='red'>Invalid No SearchResponse came back in the soap body as the root element</font></p>");
      try {
         RegistryValidator.isValid(entry);
      }catch(AssertionFailedError afe) {
            out.write("<p><font color='red'>Invalid xml (VOResources): " + afe.getMessage() + "</font></p>");
      }
      
            
      out.write("<table border=1>");
      out.write("<tr><td>AuthorityID</td><td>ResourceKey</td><td>View XML</td></tr>");
      NodeList resources = entry.getElementsByTagNameNS("*","Resource");
         
      for (int n=0; n < resources.getLength();n++) {
         out.write("<tr>\n");
         
//         Element resource = (Element) ((Element) identifiers.item(n)).getElementsByTagNameNS("*","ResourceKey").item(0);
//         Element authority = (Element) ((Element) identifiers.item(n)).getElementsByTagNameNS("*","AuthorityID").item(0);
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
         endpoint = java.net.URLEncoder.encode(endpoint,"UTF-8");

         out.write("<td><a href=externalResourceEntry.jsp?performquery=true&IVORN="+ivoStr+"&endpoint=" + endpoint + ">View</a></td>\n");
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

<% } } %>

</body>
</html>