<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.util.DomHelper,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.metadata.MetadataServer,
       org.astrogrid.datacenter.service.*"
   isThreadSafe="false"
   session="false"
%>
<html>
<%= HtmlDataServer.getHeadElement("Metadata Viewer") %>
<body>
<%= HtmlDataServer.getPageHeader() %>

<h1>Metadata for <%=DataServer.getDatacenterName() %></h1>

<!--- list tables & columns --->
<%
   MetadataServer.clearCache(); //force refresh
   try {
      String[] tables = MetadataServer.getTables();
      for (int table=0;table<tables.length;table++) {
%>
<h2>Table '<%=tables[table] %>'</h2>
<p><%= DomHelper.getValue(MetadataServer.getTableElement(tables[table]),"Description") %></p>
<p>
<table border=1 summary='Column details for table <%=tables[table] %>' cellpadding='5%'>
<tr>
<th>Column</th>
<th>Type</th>
<th>Units</th>
<th>UCD</th>
<th>Error</th>
</tr>
<%
         String[] cols = MetadataServer.getColumns(tables[table]);
         for (int col=0;col<cols.length;col++) {
            Element colElement = MetadataServer.getColumnElement(tables[table], cols[col]);
%>
<tr>
<th><%=cols[col] %></th>
<td><%=colElement.getAttribute("datatype") %></td>
<td><%=DomHelper.getValue(colElement, "Units") %></td>
<td><%=DomHelper.getValue(colElement, "UCD") %></td>
<td><%=DomHelper.getValue(colElement, "ErrorColumn") %></td>
<td><%=DomHelper.getValue(colElement, "Description") %></td>
</tr>
<%
         } //end cols
%>
</table>
<%
      } //end tables
%>

<h2>Functions available</h2>
<%
      NodeList funcList = MetadataServer.getMetadata().getElementsByTagName("Function");
      for (int i = 0; i < funcList.getLength(); i++) {
%>
         <%=DomHelper.getValue( (Element) funcList.item(i)) %>,
<%
      }
   }
   catch (FileNotFoundException e) {
%>
     (There is no metadata file specified yet for this datacenter: <%= e %>)
<%
   }
%>

<h2>Raw Metadata</h2>

<p>Download the raw metadata document <a href='metadata.jsp'>here</a></p>
<p>If you have access to administrator functions, you can download regenerated metadata <a href='admin/generateMetadata.jsp'>here</a></p>

<%= HtmlDataServer.getPageFooter() %>
</body>
</html>


