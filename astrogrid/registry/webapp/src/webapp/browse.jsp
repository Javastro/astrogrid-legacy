<%@ page import="org.astrogrid.registry.server.query.*,
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
   long offset = 0;
   String off = request.getParameter("Index");
   if (off != null) {
       offset = Long.parseLong(off);
   }
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

<form action='browse.jsp' method='get'>
<p>
Find IVORNs including <input name="IvornPart" type="text" />
<input type="submit" name="button" value='List'/>
</form>
</p>
<p>
   
<!--
<form action="browse.jsp" method="get"><input type="submit" title='Prev' name="Index" value="<%= offset-25 %>"/></form>
</td>
<td align='right'>
<form action="browse.jsp" method="get"><input type="submit" title='Next' name="Index" value="<%= offset+25 %>"/></form>
</td>
-->
<%
   RegistryQueryService server = new RegistryQueryService();
   String ivornpart = request.getParameter("IvornPart");

   out.write("*"+ivornpart+"*:<br/");
   
   Document entries = null;
   
   if (ivornpart != null) {
         String selectQuery = "<query><selectionSequence>" +
             "<selection item='searchElements' itemOp='EQ' value='all'/>" +
             "<selectionOp op='$and$'/>" +
             "<selectionSequence>"+
               "<selection item='vr:Identifier/vr:AuthorityID' itemOp='CONTAINS' value='" + ivornpart + "'/>"+
               "<selectionOp op='OR'/>" +
               "<selection item='vr:Identifier/vr:ResourceKey' itemOp='CONTAINS' value='" + ivornpart + "'/>"+
             "</selectionSequence>"+
           "</selectionSequence></query>";
   
         entries = server.submitQuery(DomHelper.newDocument(selectQuery));
   }
   else {
         String selectQuery = "<query><selectionSequence>" +
         "<selection item='searchElements' itemOp='EQ' value='all'/>" +
         "<selectionOp op='$and$'/>" +
         "<selection item='vr:Identifier/vr:AuthorityID' itemOp='NE' value=' '/>"+
         "</selectionSequence></query>";
   
         entries = server.submitQuery(DomHelper.newDocument(selectQuery));
   }
   
   if (entries == null) {
      out.write("<p>No entries?!</p>");
   }
   else {

      out.write("<table border=1>");
      out.write("<tr><td>Type</td><td>AuthorityID</td><td>ResourceKey</td><td>Actions</td></tr>");
      
      NodeList resources = entries.getElementsByTagNameNS("*","Resource");
      
      for (int n=0;n<resources.getLength();n++) {
         out.write("<tr>\n");
         
         Element resourceElement = (Element) resources.item(n);

         //type
         out.write("<td>"+resourceElement.getAttribute("xsi:type")+"</td>");

         NodeList identifiers = resourceElement.getElementsByTagNameNS("*", "Identifier");
         if (identifiers.getLength() == 0) {
            out.write("<td>ERROR: Resource has no 'Identifier' element</td>");
         }
         else {
            
            //authr
            Element resource = (Element) ((Element) identifiers.item(0)).getElementsByTagNameNS("*","ResourceKey").item(0);
            Element authority = (Element) ((Element) identifiers.item(0)).getElementsByTagNameNS("*","AuthorityID").item(0);
   
            String ivoStr = null;
            if (authority == null) {
               out.write("<td>null?!</td>");
            } else {
               String t = DomHelper.getValue(authority);
               out.write("<td>"+t+"</td>\n");
               ivoStr = t;
            }
   
            if (resource == null) {
               out.write("<td>null?!</td>");
            } else {
               String t = DomHelper.getValue(resource);
               out.write("<td>"+t+"</td>\n");
               ivoStr = ivoStr+"/"+t;
            }
   
            out.write("<td>");
            //links to do stuff
            out.write("<a href=viewResourceEntry.jsp?IVORN="+ivoStr+">View</a>, ");
   
            out.write("<a href=viewEntryXml.jsp?IVORN="+ivoStr+">XML</a>,  ");

            out.write("<a href=admin/deleteResource.jsp?IVORN="+ivoStr+">Delete</a>,  ");
            
            out.write("<a href=admin/editEntry.jsp?IVORN="+ivoStr+">Edit</a>");
            
            out.write("</td>");
         }
         out.write("</tr>\n");
         
      }
      
      out.write("</table>");
   
   }
%>


</body>
</html>
