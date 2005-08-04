<%@ page import="org.astrogrid.registry.server.admin.*,
					  org.astrogrid.registry.server.query.*,
					  org.astrogrid.registry.server.harvest.*,
					  org.astrogrid.registry.server.*,
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
   RegistryQueryService server = new RegistryQueryService();
   RegistryHarvestService rhs = new RegistryHarvestService();
   ArrayList al = server.getAstrogridVersions();
      String version = request.getParameter("version");
	   if(version == null || version.trim().length() <= 0) {
   		version = RegistryDOMHelper.getDefaultVersionNumber();
   	}
%>

<html>
<head>
<title>Harvest Resource</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
<!-- European format dd-mm-yyyy -->
<script language="JavaScript" src="calendar1.js"></script><!-- Date only with year scrolling -->

</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>
<h1>Harvest Registry Resource</h1>

<p>Harvest a particular Registry Resource.</p>

<form method='get'>
<p>
Look for another version
<select name="version">
   <% for(int k = (al.size()-1);k >= 0;k--) { %>
      <option value="<%=al.get(k)%>"
        <%if(version.equals(al.get(k))) {%> selected='selected' <%}%> 
      ><%=al.get(k)%></option>  
   <%}%>
</select>
<input type="submit" name="button" value='List'/>
</p>
</form>

<p>
<%
   //out.write("*"+ivornpart+"*:<br/");
   
   Document entries = null;
	entries = server.getRegistriesQuery(version);
	
	String doHarvest = request.getParameter("doharvest");
	if(doHarvest != null && doHarvest.trim().length() > 0) {
		String ivornHarvest =  request.getParameter("ident");
		System.out.println("inside doHarvest and ivorn = " + ivornHarvest);
		if(ivornHarvest == null || ivornHarvest.trim().length() <= 0) {
			out.write("<p><font color='red'>Error cannot find Ivorn on request</font></p>");
		}
		else {		
			Date parsedDate = null;
		   String dt = request.getParameter("input1");
		   if(dt != null && dt.trim().length() > 0) {
			   dt = dt.trim();
			   SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				parsedDate = df.parse(dt);
			//   System.out.println("the actual date = " + df.format(parsedDate));
			//   SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
			//   System.out.println("the actual date sdf2 = " + sdf2.format(parsedDate));
			}
		   
			Document harvestEntry = server.getResourcesByIdentifier(request.getParameter("ident").trim(),version);
			rhs.beginHarvest(harvestEntry.getDocumentElement(),parsedDate,version);
		}
	}
   
   if (entries == null) {
      out.write("<p>No entries?!</p>");
   }
   else {
		out.write("<form name='harvestform' method='post'>");
		
		out.write("Select Date (w. year scrl.):");
		out.write("<input type=\'text\' name=\'input1\' value=''>");
		out.write("<a href=\"javascript:cal1.popup();\"><img src=\"img/cal.gif\" width=\"16\" height=\"16\" border=\"0\" alt=\"Click Here to Pick up the date\"></a><br>");
		
		
      out.write("<table border=1>");
      out.write("<tr><th>Title</th><th>Type</th><th>AuthorityID</th><th>ResourceKey</th><th>Updated</th><th>Actions</th></tr>");
      
      NodeList resources = entries.getElementsByTagNameNS("*","Resource");

		out.write("<input type='hidden' name='version' value='" + version + "' />");
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
	     if(resourceElement.getAttribute("status") != null)
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

			if("0_10".equals(version)) {         
	         out.write("<td>");
            out.write("<input type='radio' name='ident' value='" + ivoStr + "' />");
	         out.write(" " + setFG+DomHelper.getValue(resourceElement, "title")+endFG+"</td>");
	      }else {
	         out.write("<td>");	      
            out.write("<input type='radio' name='ident' value='" + ivoStr + "' />");
	         out.write(" "+setFG+DomHelper.getValue(resourceElement, "Title")+endFG+"</td>");
	      }
         
         //type
         out.write("<td>"+setFG+resourceElement.getAttribute("xsi:type")+endFG+"</td>");

            

            if (authority == null || authority.trim().length() <= 0) {
               out.write("<td>null?!</td>");
            } else {
               out.write("<td><a href='browse.jsp?version="+version+"&IvornPart="+authority+"'>"+authority+"</a></td>\n");
            }
   
            if (resource == null || resource.trim().length() <= 0) {
               out.write("<td>null?!</td>");
            } else { 
               out.write("<td>"+setFG+resource+endFG+"</td>\n");
            
            }
   
            //last update date
            out.write("<td>"+setFG+resourceElement.getAttribute("updated")+endFG+"</td>");
            
            out.write("<td>");
   
            out.write("<a href=../viewResourceEntry.jsp?version="+version+"&IVORN="+ivoStr+">XML</a>,  ");
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
