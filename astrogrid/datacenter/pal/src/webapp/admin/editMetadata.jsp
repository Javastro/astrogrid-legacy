<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.util.DomHelper,
       org.apache.commons.logging.LogFactory,
       org.astrogrid.community.Account,
       org.astrogrid.datacenter.metadata.*,
       org.astrogrid.datacenter.service.*"
   isThreadSafe="false"
   session="false"
%>
<html>
<%= HtmlDataServer.getHeadElement("Metadata Viewer") %>
<body>
<jsp:include page='../navigation.xml' flush='true'>
<jsp:include page='../header.xml' flush='true'>

<h1>Metadata for <%=DataServer.getDatacenterName() %></h1>

<form action='mergeEditMeta.jsp'>
<!--- list tables & columns --->
<%
   Document metadata = null;
   try {
      metadata = MetadataServer.getMetadata();
   }
   catch (FileNotFoundException fnfe) {
      metadata = MetadataGenerator.generateMetadata();
      %>
      <p>Note that a configured metadata file could not be found (<%= fnfe %>)
      so this is based on the plugin's automatically generated metadata.
      <%
   }

   try {
      NodeList tableNodes = metadata.getElementsByTagName("Table");
      for (int table=0;table<tableNodes.getLength();table++) {
         String name = ((Element) tableNodes.item(table)).getAttribute("name");
%>
<h2>Table '<%=name %>'</h2>
<p><%= DomHelper.getValue((Element) tableNodes.item(table), "Description") %></p>
<p>
<table border=1 summary='Column details for table <%=name %>' cellpadding='5%'>
<tr>
<th>Column</th>
<th>Type</th>
<th><a href="http://vizier.u-strasbg.fr/doc/catstd-3.2.htx">Units</a></th>
<th><a href="http://vizier.u-strasbg.fr/viz-bin/UCDs">UCD1</a></th>
<th>UCD1+</th>
<th>Error</th>
</tr>
<%
         NodeList colNodes = ((Element) tableNodes.item(table)).getElementsByTagName("Column");
         for (int col=0;col<colNodes.getLength();col++) {
            Element colElement = (Element) colNodes.item(col);
%>
<tr>
<th><%=colElement.getAttribute("name") %></th>
<td><%=colElement.getAttribute("datatype") %></td>
<td><input  type='text' name='units' value='<%= DomHelper.getValue(colElement, "Units") %>'></td>
<td><input  type='text' name='ucd' value='<%=DomHelper.getValue(colElement, "UCD") %>'></td>
<td><input  type='text' name='ucdPlus' value='<%=DomHelper.getValue(colElement, "UcdPlus") %>'></td>
<td><%=DomHelper.getValue(colElement, "ErrorColumn") %></td>
</tr>
<%
         } //end cols
%>
</table>
<%
      } //end tables
%>
  <input type='submit' name='Merge' value='Generate Metadata'>
</form>

<h2>Functions available</h2>
<%
      NodeList funcList = MetadataServer.getMetadata().getElementsByTagName("Function");
      for (int i = 0; i < funcList.getLength(); i++) {
%>
         <%=DomHelper.getValue( (Element) funcList.item(i)) %>,


<h2>Raw Metadata</h2>

<p>Download the raw metadata document <a href='metadata.jsp'>here</a></p>

<%
      }
   }
   catch (FileNotFoundException e) {
%>
     (There is no metadata file specified yet for this datacenter: <%= e %>)
<%
   }
%>
<p>If you have access to administrator functions, you can download regenerated metadata <a href='admin/generateMetadata.jsp'>here</a></p>

<jsp:include page='../footer.xml' flush='true'>
</body>
</html>


