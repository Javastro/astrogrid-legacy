<%@ page import="org.astrogrid.registry.server.query.*,
				 org.astrogrid.registry.server.*,
				 org.astrogrid.registry.common.RegistryDOMHelper,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.w3c.dom.Element,
                 org.w3c.dom.NodeList,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Browse Registred Resources</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<%

   RegistryQueryService server = new RegistryQueryService();
   ArrayList al = server.getAstrogridVersions();

   String version = request.getParameter("version");
   if(version == null || version.trim().length() <= 0) {
   	version = RegistryDOMHelper.getDefaultVersionNumber();
   }


   long offset = 0;
   String off = request.getParameter("Index");
   if (off != null) {
       offset = Long.parseLong(off);
   }
   String ivornpart = request.getParameter("IvornPart");
   if (ivornpart == null) { ivornpart = ""; }
%>

<h1>Registry Browser</h1>

<!-- Navigation keys/controls -->
<!--
<table width="100%">
<tr>
<td align='left'>
<%
//   if (offset>0) {
//      out.write("<a href='browse.jsp?Index="+(offset-25)+"'>Prev</a>");
//   }
%>
</td>
<td align='right'>
<a href='browse.jsp?Index=<%= (offset+25) %>'>Next</a>
</td>
</table>
-->

<form method='get'>
<p>
Version: 
<select name="version">
   <% for(int k = (al.size()-1);k >= 0;k--) { %>
      <option value="<%=al.get(k)%>"
        <%if(version.equals(al.get(k))) {%> selected='selected' <%}%> 
      ><%=al.get(k)%></option>  
   <%}%>
</select>
<br/>
Find IVORNs including: <input name="IvornPart" type="text" value='<%= ivornpart %>'/>
<input type="submit" name="button" value='List'/>
</p>
</form>

<form method='get'>
<p>
Browse for another version
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
   
<!--
<form action="browse.jsp" method="get"><input type="submit" title='Prev' name="Index" value="<%= offset-25 %>"/></form>
</td>
<td align='right'>
<form action="browse.jsp" method="get"><input type="submit" title='Next' name="Index" value="<%= offset+25 %>"/></form>
</td>
-->
<%
   //out.write("*"+ivornpart+"*:<br/");
   
   Document entries = null;
//   System.out.println("version in browse.jsp=" + version);
   
   if ( (ivornpart != null) && (ivornpart.trim().length() > 0) ) {
   		 entries = server.getResourcesByAnyIdentifier(ivornpart,version);
   }
   else {
   		 entries = server.getAll(version);
   }
   
   if (entries == null) {
      out.write("<p>No entries?!</p>");
   }
   else {

      out.write("<table border=1>");
      out.write("<tr><th>Title</th><th>Type</th><th>AuthorityID</th><th>ResourceKey</th><th>Updated</th><th>Actions</th></tr>");
      
      NodeList resources = entries.getElementsByTagNameNS("*","Resource");

      for (int n=0;n<resources.getLength();n++) {
         Element resourceElement = (Element) resources.item(n);
	     	boolean deleted = false; 
	     	boolean inactive = false;
	     	if(resourceElement.getAttribute("status") != null)
		  	deleted = resourceElement.getAttribute("status").toLowerCase().equals("deleted");         
		  	inactive = resourceElement.getAttribute("status").toLowerCase().equals("inactive");
         String bgColour = "#FFFFFF";
         String fgColour = "#000000";
         
         if (deleted) {
            bgColour = "#FFFFFF";
            fgColour = "#AAAAAA";
         }
         if (inactive) {
            bgColour = "#FFFFFF";
            fgColour = "#AAAAAA";
         }         
         String setFG = "<font color='"+fgColour+"'>";
         String endFG = "</font>";
         
         out.write("<tr bgcolor='"+bgColour+"'>\n");

			if("0.9".equals(version)) {
				out.write("<td>"+setFG+DomHelper.getValue(resourceElement, "Title")+endFG+"</td>");
	      }else {
  	         out.write("<td>"+setFG+DomHelper.getValue(resourceElement, "title")+endFG+"</td>");	         
	      }
         
         //type
         String xsiType = resourceElement.getAttribute("xsi:type");
         out.write("<td>"+setFG+xsiType+endFG+"</td>");
         if(xsiType.indexOf(":") != -1) {
           xsiType = xsiType.substring(xsiType.indexOf(":")+1);
         }
            //authr
				String authority = RegistryDOMHelper.getAuthorityID(resourceElement);
				String resource = RegistryDOMHelper.getResourceKey(resourceElement);
   
            String ivoStr = null;
            if (authority == null || authority.trim().length() <= 0) {
               out.write("<td>null?!</td>");
            } else {
               out.write("<td><a href='browse.jsp?version="+version+"&IvornPart="+authority+"'>"+authority+"</a></td>\n");
               ivoStr = authority;
            }
   
            if (resource == null || resource.trim().length() <= 0) {
               out.write("<td>null?!</td>");
            } else { 
               out.write("<td>"+setFG+resource+endFG+"</td>\n");
               ivoStr = ivoStr+"/"+resource;
            }
            ivoStr = java.net.URLEncoder.encode(("ivo://" + ivoStr),"UTF-8");
   
            //last update date
            out.write("<td>"+setFG+resourceElement.getAttribute("updated")+endFG+"</td>");
            
            out.write("<td>");
   
            out.write("<a href=viewResourceEntry.jsp?version="+version+"&IVORN="+ivoStr+">XML,</a>  ");

            out.write("<a href=editEntry.jsp?version="+version+"&IVORN="+ivoStr+">Edit,</a>");
            out.write("<a href=xforms/XFormsProcessor.jsp?version="+version+"&mapType="+xsiType+"&IVORN="+ ivoStr + ">XEdit</a>");

			/*
            if (!deleted) {
               out.write(", <a href=admin/deleteResource.jsp?IVORN="+ivoStr+">Delete</a>");
            }
        	*/
            out.write("</td>");
         out.write("</font></tr>\n");
      }
      out.write("</table>");
   
   }
%>


</body>
</html>
