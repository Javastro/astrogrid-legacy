<%@ page import="org.astrogrid.registry.server.admin.*,
				 org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
                 java.net.URL,
 	  		     org.astrogrid.registry.server.http.servlets.helper.JSPHelper,                 
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 org.apache.axis.utils.XMLUtils,
                 java.util.Date,
                 java.util.Locale,
					  java.text.*,
                 java.io.*"
   isThreadSafe="false"
   session="false" %>

<head>
<title>Get Registry Resource</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
<!-- European format dd-mm-yyyy -->
<script language="JavaScript" src="calendar1.js"></script><!-- Date only with year scrolling -->

</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>
<h1>Get Registry Type Resource From Harvest URL</h1>
<p>
<font color='blue'>Be sure your Contract Version is set correctly.  Check Menu for current contract status.</font><br />
Normally you only need to use this page one time for the RofR, because all Registry Type Resources are listed in what is known as the 
Registry of Registries (RofR).  But occasionally on private registries not known to the RofR or other situations which may cause
a registry to not be in the RofR.  You may use this JSP to add the Registry Type Resource to 'this' Registry.  Once that
is done then you may go and harvest it via the harvestResource page on the menu or let the automatic harvest pick it up. 
A default url of the current RofR is below just click Submit to pick up the RofR.  For other registries put in the OAI harvest url below.<br /><br />
For astrogrid registries this is typcially:<br /> http://{server}:{port}/{context}/OAIHandlerv{contractversion ex:1_0}?verb=Identify  this will 
get the 1 (and only 1) Registry type entry to be harvested.  To get all Registries then '?verb=ListRecords&set=ivo_Registry&metadataPrefix=ivo_vor'
<br />
<p>
If you are trying to harvest 0.10 entries then suggest this:
http://galahad.star.le.ac.uk:8080/astrogrid-registry/OAIHandlerv0_1?verb=ListRecords&set=ivo_Registry&metadataPrefix=ivo_vor
<br />
This will get all the Registry types that the full registry galahad has and allow you to start harvesting those 
registries.  Alterntatively you can do verb=Identify to get just the singly Registry type Resource to harvest that particular registry.
</p>
<form name="harvestURL" method="post">
URL:
<input type="text" width=500 name="harvesturl" value="http://nvo.ncsa.uiuc.edu/cgi-bin/rofr/oai.pl"></input>
<br />
<input type="submit" value="Submit" />
</form>

<%

   //this version number is the actual resoruce version like 0.10  not the contract version.
   if(request.getParameter("harvesturl") != null) {
	    ISearch rqs = JSPHelper.getQueryService(request);
	    String reqHarvestURL = request.getParameter("harvesturl");
	    if(reqHarvestURL.indexOf("?") == -1) {
	    	reqHarvestURL += "?verb=Identify";
	    }
		URL url = new URL(reqHarvestURL);
		Document identDoc = DomHelper.newDocument(url);
 	    IAdmin ras = JSPHelper.getAdminService(request);
		Document resultDoc = null;
		resultDoc = ras.updateInternal(identDoc);
		if(resultDoc.getElementsByTagNameNS("*","Fault").getLength() > 0) {
			out.write("<br /><br /><pre>");
			out.write(DomHelper.DocumentToString(resultDoc));
			out.write("</pre>");
		}
   }
%>
