<%@ page import="org.astrogrid.registry.server.admin.*,
					  org.astrogrid.registry.server.query.*,
					  org.astrogrid.registry.server.admin.*,
					  org.astrogrid.registry.server.*,
					  org.astrogrid.registry.registration.*,
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
   
%>

<html>
<head>
<title>Harvest Resource VOSI</title>
<style type="text/css" media="all">
   <%@ include file="/style/astrogrid.css" %>          
</style>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>
<h1>Harvest Resource VOSI Contents</h1>

<p>Harvest a particular Resource via VOSI.</p>
<p>
<%
	Document entries = DomHelper.newDocument(server.getQueryHelper().getVOSIQuery().getMembersAsResource().getContent().toString());
	
	String doHarvest = request.getParameter("doharvest");
	if(doHarvest != null && doHarvest.trim().length() > 0) {
		IAdmin admin = JSPHelper.getAdminService(request);	  
		String ivornHarvest =  request.getParameter("ident");
		if(ivornHarvest == null || ivornHarvest.trim().length() <= 0) {
			out.write("<p><font color='red'>Error cannot find Ivorn on request</font></p>");
		}
		else {
			Document harvestEntry = server.getQueryHelper().getResourceByIdentifier(request.getParameter("ident").trim());
			Node vosiRes = new VOSIHarvest().harvestCapabilities(harvestEntry.getElementsByTagNameNS("*","Resource").item(0));
			System.out.println("in harvestvosi.jsp got the vosiRes node lets update");
			Document result = admin.updateInternal((Document)vosiRes);
			System.out.println("sent to updateInternal");
			if (result != null && result.getDocumentElement().hasChildNodes()) {
			    System.out.println("result seemed to have an error");
			    out.write("<font color='red'>");
			    out.write(DomHelper.DocumentToString(result));
        		out.write("</font>");
		    }else {
		        System.out.println("ok success lets print a success message");
		   		out.write("<font color='blue'>Success in update, click on xml below to view</font>"); 
		    }
		}
	}
   
   if (entries == null) {
      out.write("<p>No entries?!</p>");
   }
   else {
		out.write("<form name='harvestform' method='post'>");
		
      out.write("<table border=1>");
      out.write("<tr><th>Title</th><th>Type</th><th>AuthorityID</th><th>ResourceKey</th><th>Updated</th><th>Actions</th></tr>");
      
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
   
            out.write("<a href=../viewResourceEntry.jsp?IVORN="+ivoStr+">XML</a> ");
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
</div>
<%@ include file="/style/footer.xml" %>


</body>
</html>
