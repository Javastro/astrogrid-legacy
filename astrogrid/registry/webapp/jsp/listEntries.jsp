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
<head><title>List of Registry Entries</title>
</head>

<body>

<h1>Registry Entries</h1>

<hr>
<%
   RegistryQueryService server = new RegistryQueryService();

         String selectQuery = "<query><selectionSequence>" +
         "<selection item='searchElements' itemOp='EQ' value='all'/>" +
         "<selectionOp op='$and$'/>" +
         "<selection item='*:Identifier/*:AuthorityID' itemOp='NE' value=' '/>"+
         "</selectionSequence></query>";

   
   Document query = DomHelper.newDocument(selectQuery);
   
   Document entry = server.submitQuery(query);
   
   if (entry == null) {
      out.write("<p>No entries?!</p>");
   }
   else {
   
      out.write("<table border=1>");
      out.write("<tr><td>AuthorityID</td><td>ResourceKey</td><td>Actions</td></tr>");
      
      
      NodeList identifiers = entry.getElementsByTagNameNS("*","Identifier");
      
      for (int n=0;n<identifiers.getLength();n++) {
         out.write("<tr>\n");
         
//         NodeList d2 = ((Element) identifiers.item(n)).getElementsByTagNameNS("*""ResourceKey");
//         out.write("Item 0:"+d2.item(0).getNodeName()+", "+d2.item(0).getFirstChild().getNodeValue()+"\n");
//         out.write("Item 1:"+d2.item(1).getNodeName()+", "+d2.item(1).getNodeValue()+"\n");
         
         
         Element resource = (Element) ((Element) identifiers.item(n)).getElementsByTagNameNS("*","ResourceKey").item(0);
         Element authority = (Element) ((Element) identifiers.item(n)).getElementsByTagNameNS("*","AuthorityID").item(0);
         
         if (authority == null) {
            out.write("<td>null?!</td>");
         } else {
            out.write("<td>"+authority.getFirstChild().getNodeValue()+"</td>\n");
         }

         if (resource == null) {
            out.write("<td>null?!</td>");
         } else {
            out.write("<td>"+resource.getFirstChild().getNodeValue()+"</td>\n");
         }
         
         out.write("<td><a href=viewResourceEntry.jsp?IVORN="+authority.getFirstChild().getNodeValue()+">View</a></td>\n");
         
         out.write("</tr>\n");
         
      }
      
      out.write("</table>");
   

%>
<hr>
Use your browser's 'view source' to view the XML:
<pre>
<%
      DomHelper.DocumentToWriter(entry, out);
    }
%>
</pre>

<a href="index.html">Index</a>


</body>
</html>
