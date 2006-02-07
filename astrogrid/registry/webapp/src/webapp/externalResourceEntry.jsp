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
   isThreadSafe="false"
   session="false" %>
   
html>
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

<h1>Get Resource</h1>

<form method="post">
<p>
<br />Endpoint: <input type="text"   size="100"  name="endpoint" value="<%= request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath() %>/services/RegistryQuery" /><br />
<input type="hidden" name="performquery" value="true" />
Identifier: <input type="text" name="IVORN" value="ivo://" /><br />
<br />
<input type="submit" name="button" value="Submit"/><br />
</p>

</form>   
   
<pre>
<%
      String performQuery = request.getParameter("performquery");
      if("true".equals(performQuery)) {
      String endpoint = request.getParameter("endpoint");
      String ident = request.getParameter("IVORN");
      System.out.println("this is the endpoint = " + endpoint + " and ident = " + ident);
      if(endpoint == null || endpoint.trim().length() == 0) 
        out.write("<p><font color='red'>No endpoint given</font></p>");
        
      if(ident == null || ident.trim().length() == 0) 
        out.write("<p><font color='red'>Identifier is empty.</font></p>");
        

      RegistryService rs = RegistryDelegateFactory.createQuery(new URL(endpoint));
      Document entry = rs.getResourceByIdentifier(ident);
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

         out.write("<td><a href=externalResourceEntry.jsp?"IVORN="+ivoStr+"&endpoint=" + endpoint + ">View</a></td>\n");         
         out.write("</tr>\n");
      }                  
         out.write("</table> <hr />");
         out.write("The xml<br />");
         String testxml = DomHelper.DocumentToString(entry);
         testxml = testxml.replaceAll("<","&lt;");
        testxml = testxml.replaceAll(">","&gt;");
         out.write(testxml);
      }//else
      
      }
      
%>
</pre>

</body>
</html>