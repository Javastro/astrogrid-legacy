<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.datacenter.ucd.UcdDictionary,
       org.astrogrid.util.DomHelper,
       org.astrogrid.datacenter.metadata.MetadataServer,
       org.astrogrid.datacenter.service.*"
   isThreadSafe="false"
   session="false"
%>
<html>
<head>
<title>Metadata for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/maven-base.css");
          @import url("./style/maven-theme.css");
</style>
</title>
</head>


<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Metadata for <%=DataServer.getDatacenterName() %></h1>

<!--- list tables & columns --->
<%
   MetadataServer.clearCache(); //force refresh
   try {
      String[] tables = MetadataServer.getTables();
      for (int table=0;table<tables.length;table++) {
%>
<h2>Table '<%=tables[table] %>' - <%= DomHelper.getValue(MetadataServer.getTableElement(tables[table]),"Title") %> </h2>
<p><%= DomHelper.getValue(MetadataServer.getTableElement(tables[table]),"Description") %></p>
<p>
<table border=1 summary='Column details for table <%=tables[table] %>' cellpadding='5%'>
<tr>
<th>Column</th>
<th>Type</th>
<th>Units</th>
<th><a href='<%= UcdDictionary.UCD1REF %>'>UCD1</a></th>
<th><a href='<%= UcdDictionary.UCD1PREF %>'>UCD1+</a></th>
<th>Error</th>
<th>Description</th>
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
<td><%=DomHelper.getValue(colElement, "UcdPlus") %></td>
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
</div>

   <%@ include file="footer.xml" %>
</body>
</html>


