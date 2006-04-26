<%@ page import="org.astrogrid.registry.server.IChecker,
					  org.astrogrid.registry.server.CheckerFactory,
					  org.astrogrid.registry.server.CheckerException,
                 org.astrogrid.registry.client.query.RegistryService,
				     org.astrogrid.registry.server.http.servlets.helper.JSPHelper,
                 org.astrogrid.registry.client.RegistryDelegateFactory,
                 org.astrogrid.registry.client.RegistryDelegateFactory,
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


<br />

<form method="post">
<p>
Paste ADQL xml:<br />
<br />Endpoint: <input type="text"   size="100"  name="endpoint" value="<%= request.getScheme()+"://"+request.getServerName() +":" + request.getServerPort()+request.getContextPath() %>/services/RegistryQuery" /><br />
<input type="hidden" name="performquery" value="true" />
Keywords: <input type="text" name="keywords" /><br />
Search for "any" of the words: <input type="checkbox" name="orValues" value="true">Any/Or the words</input>
<br />
<input type="submit" name="button" value="Submit"/><br />
</p>

</form>

<br />

<pre>
<%
	String performQuery = request.getParameter("performquery");
   String endpoint = request.getParameter("endpoint");
   String keywords = request.getParameter("keywords");
   if("true".equals(performQuery)) {
   	System.out.println("performQuery true and endpoint = " + request.getParameter("endpoint"));

      if(endpoint == null || endpoint.trim().length() == 0) {
        out.write("<p><font color='red'>No endpoint given</font></p>");
        performQuery = "false";
      }
   	if(keywords == null || keywords.trim().length() == 0) {
     		out.write("<font color='red'>You have no keywords, please place keywords in the text box to do a query</font>");
     		performQuery = "false";
   	}
   }//if
		if("true".equals(performQuery)) {
		   String contractVersion = JSPHelper.getQueryContractVersion(request);
		   IChecker checker = CheckerFactory.createQueryChecker(contractVersion);
	      
	      boolean orValue = new Boolean(request.getParameter("orValues")).booleanValue();
	      RegistryService rs = RegistryDelegateFactory.createQuery(new URL(endpoint));
	      Document entry = rs.keywordSearch(keywords,orValue);
	      out.write("<p>If entries are returned, then the xml will be validated, shown tabular, then full xml at the bottom.");
	      boolean valid = false;
	      try {
		      valid = checker.checkValidResources(entry);
		   }catch(CheckerException ce) {
		     out.write("<p><font color='red'>Invalid to Schema: " + ce.getMessage() + "</p>");
		   }
		   try {
		      valid = checker.checkValidWSContract(entry,"SearchResponse");
		   }catch(CheckerException ce) {
			   out.write("<p><font color='red'>Invalid to Web Service Call: " + ce.getMessage() + "</p>");
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
	
	         out.write("<td><a href=externalResourceEntry.jsp?performquery=true&IVORN="+ivoStr+"&endpoint=" + endpoint + ">View</a></td>\n");
	         
	         out.write("</tr>\n");
	         
	      }                  
	         out.write("</table> <hr />");
	         out.write("The xml<br />");
	         String testxml = DomHelper.DocumentToString(entry);
	         testxml = testxml.replaceAll("<","&lt;");
	         testxml = testxml.replaceAll(">","&gt;");
	         out.write(testxml);
	   }//if
%>
</pre>
</body>
</html>