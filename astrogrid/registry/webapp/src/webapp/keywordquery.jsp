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
<title>Keyword Search Query</title>
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
   Keyword search.  Place words seperated by spaces. <br />
</p>

<form method="post">
<input type="hidden" name="keywordquery" value="true" />
Keywords: <input type="text" name="keywords"/><br />
Search for "any" of the words: <input type="checkbox" name="orValues" value="true">Any/Or the words</input>
<input type="submit" name="keywordquerysubmit" value="Query" />
</form>


<%
  String error = "";
  boolean doQuery = false;
  String keywords = null;
  boolean orValue = false;
  if(request.getParameter("keywordquery") != null && request.getParameter("keywordquery").trim().equals("true")) {
   if(request.getParameter("keywords") != null && request.getParameter("keywords").trim().length() > 0) {
      keywords = request.getParameter("keywords");
      orValue = new Boolean(request.getParameter("orValues")).booleanValue();
      doQuery = true;
   }else {
      error = "Cannot find any words to query";
   }
  
  
  String maxCount = SimpleConfig.getSingleton().getString("exist.query.returncount", "25");
%>
<br />

<p>
<font color="red"><%=error%></font>
</p>

<strong>If no errors the Query results will be below:<br />Only a max of <%= maxCount %> entries will be shown:</strong><br />

<pre>
<%
   if(doQuery) {
      RegistryQueryService server = new RegistryQueryService();
      Document entry = server.keywordQuery(keywords,orValue);
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
         
      }//for
         
         out.write("</table> <hr />");
      
         out.write("The xml<br />");
        String testxml = DomHelper.DocumentToString(entry);
         testxml = testxml.replaceAll("<","&lt;");
        testxml = testxml.replaceAll(">","&gt;");
         out.write(testxml);
      }//else
   }//if
      
%>
</pre>

<% } %>

</body>
</html>