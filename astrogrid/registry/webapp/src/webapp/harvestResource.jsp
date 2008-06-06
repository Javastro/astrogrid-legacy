<%@ page import="org.astrogrid.registry.server.admin.*,
					  org.astrogrid.registry.server.query.*,
					  org.astrogrid.registry.server.harvest.*,
					  org.astrogrid.registry.server.*,
 	  				  org.astrogrid.registry.server.http.servlets.helper.JSPHelper,					  
					  org.astrogrid.registry.common.RegistryDOMHelper,
					  java.text.*,					  
                 org.w3c.dom.*,
                 org.astrogrid.util.DomHelper,
                 java.util.ArrayList,
                 java.util.Date,
                 java.util.Locale,
                 java.io.*"
    session="false" %>
    
<%
   ISearch server = JSPHelper.getQueryService(request);
   IHarvest rhs = JSPHelper.getHarvestService(request);
%>

<html>
<head>
	<title>Harvest Status Information</title>
	<style type="text/css" media="all">
	   <%@ include file="/style/astrogrid.css" %>          
	</style>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>
<h1>Harvest Status Information</h1>

<p>Harvest Status.</p>
<p>
<%
	Document entries = DomHelper.newDocument(server.getQueryHelper().getRegistriesQuery().getMembersAsResource().getContent().toString());
	RegistryHarvestAdmin rha = new RegistryHarvestAdmin();
   	//Document entry = rha.getStatus(request.getParameter("IVORN"),versionNumber);
	String versionNumber = server.getResourceVersion();
	
   if (entries == null) {
      out.write("<p>No entries?!</p>");
   }
   else {
      out.write("<table border=1>");
      out.write("<tr><th>Title</th><th>Type</th><th>Identifier</th><th>Last Harvest</th><th>Summary</th><th>Actions</th></tr>");
      
      NodeList resources = entries.getElementsByTagNameNS("*","Resource");

  	  out.write("<input type='hidden' name='doharvest' value='true' />");
      for (int n=0;n<resources.getLength();n++) {
      
         Element resourceElement = (Element) resources.item(n);
  		 String authority = RegistryDOMHelper.getAuthorityID(resourceElement);
		 String resource = RegistryDOMHelper.getResourceKey(resourceElement);
         String ivoStr = "ivo://" + authority;
         if (resource != null || resource.trim().length() > 0) {
	     	   ivoStr = ivoStr+"/"+resource;
         }
         Document statEntry = rha.getStatus(ivoStr,versionNumber);
         String lastHarvestDate = "";
         if(statEntry.getElementsByTagName("StatsDate").length() > 0) {
          	DomHelper.getTextNode((Element)statEntry.getElementsByTagName("StatsDate").item(0));        
         }
         String summaryInfo = "";
         NodeList childElems;
         if((childElems = statEntry.getElementsByTagName("Info")).length() > 0) {
         	summary += "Harvested " +  
         	 DomHelper.getTextNode((Element)childElems.item( (childElems.length() -1 )) + " on " +
         	 (Element)childElems.item( (childElems.length() -1 ).getAttribute("date");
         }
         if((childElems = statEntry.getElementsByTagName("Error")).length() > 0) {
         	summary += "Error on " +  
         	 (Element)childElems.item( (childElems.length() -1 ).getAttribute("date");
         }
         
         
	     boolean deleted = false; 
	     if(resourceElement.getAttribute("status").length() > 0)
		  	deleted = resourceElement.getAttribute("status").toLowerCase().equals("deleted");
         
         String bgColour = "#FFFFFF";
         String fgColour = "#000000";
         
         if (deleted) {
            bgColour = "#FFFFFF";
            fgColour = "#AAAAAA";
         }
         String setFG = "<font color='"+fgColour+"'>";
         String endFG = "</font>";
         
         out.write("<tr bgcolor='"+bgColour+"'>\n");
	     out.write("<td>");
         out.write("<input type='radio' name='ident' value='" + ivoStr + "' />");
	     out.write(" " + setFG+DomHelper.getValue(resourceElement, "title")+endFG+"</td>");
         
         //type
         out.write("<td>"+setFG+resourceElement.getAttribute("xsi:type")+endFG+"</td>");
			if (authority == null || authority.trim().length() <= 0) {
               out.write("<td>null?!</td>");
            } else {
               out.write("<td><a href='browse.jsp?IvornPart="+authority+"'>"+authority+"</a></td>\n");
            }
   
            if (resource == null || resource.trim().length() <= 0) {
               out.write("<td>null?!</td>");
            } else { 
               out.write("<td>"+setFG+resource+endFG+"</td>\n");
            
            }
   
            //last update date
            out.write("<td>"+setFG+resourceElement.getAttribute("updated")+endFG+"</td>");
            
            out.write("<td>");
   
            out.write("<a href=../viewResourceEntry.jsp?IVORN="+ivoStr+">XML</a>,  ");
            out.write(" <a href=harvestStatus.jsp?IVORN="+ivoStr+">Status</a>,  ");
            out.write("</td>");                        
         out.write("</font></tr>\n");
      }
      out.write("</table>");
      out.write("<input type='submit' name='harvestsubmit' value='Harvest' />");
   	out.write("</form>");
   }
%>
			<script language="JavaScript">
			<!-- // create calendar object(s) just after form tag closed
				 // specify form element as the only parameter (document.forms['formname'].elements['inputname']);
				 // note: you can have as many calendar objects as you need for your application
				var cal1 = new calendar1(document.forms['harvestform'].elements['input1']);
				cal1.year_scroll = true;
				cal1.time_comp = false;
			//-->
			</script>


</body>
</html>
