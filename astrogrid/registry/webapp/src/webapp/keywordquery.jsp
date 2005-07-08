<%@ page import="org.astrogrid.registry.server.query.*,
				 org.astrogrid.registry.server.*,
				 org.astrogrid.registry.common.RegistryDOMHelper,
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
<%
      RegistryQueryService server = new RegistryQueryService();
      ArrayList al = server.getAstrogridVersions();
      String version = request.getParameter("version");
	   if(version == null || version.trim().length() <= 0) {
   		version = RegistryDOMHelper.getDefaultVersionNumber();
   	}
            
%>
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
Version: 
<select name="version">
   <% for(int k = (al.size()-1);k >= 0;k--) { %>
      <option value="<%=al.get(k)%>"
        <%if(version.equals(al.get(k))) {%> selected='selected' <%}%> 
      ><%=al.get(k)%></option>  
   <%}%>
</select>
<br />
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

      
      Document entry = null;
      if(version != null || version.trim().length() > 0)
	      entry = server.keywordQuery(keywords,orValue,version);
	  else
	      entry = server.keywordQuery(keywords,orValue);
	      
      if (entry == null) {
        out.write("<p>No entry returned</p>");
      }
      else {
      
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
         
         String xsiType = ((Element)resources.item(n)).getAttribute("xsi:type");
         if(xsiType.indexOf(":") != -1) {
           xsiType = xsiType.substring(xsiType.indexOf(":")+1);
         }         

         out.write("<td><a href=viewResourceEntry.jsp?version="+version+"&IVORN="+ivoStr+">View,</a>");
         out.write("<a href=editEntry.jsp?version="+version+"&IVORN="+ivoStr+">Edit,</a>");
         out.write("<a href=xforms/XFormsProcessor.jsp?version="+version+"&mapType="+xsiType+"&IVORN="+ ivoStr + ">XEdit</a></td>");         
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