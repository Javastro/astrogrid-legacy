<%-- a JSP that displays the metadata in a form suitable for users.  This could/should
  well be an Xslt - I just can't be bothered making one, this is easier for me... --%>
<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.datacenter.ucd.UcdDictionary,
       org.astrogrid.datacenter.units.UnitDictionary,
       org.astrogrid.util.DomHelper,
       org.astrogrid.datacenter.metadata.*,
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
<%
   VoDescriptionServer.clearCache(); //force refresh
   //Document voDescription = VoDescriptionServer.getVoDescription();
%>
<h2>Authority</h2>
<%
   Element authorityResource = VoDescriptionServer.getAuthorityResource();
   if (authorityResource == null) {
      out.write("WARNING: Authority Resource is missing");
   }
   else {
      Element identity = (Element) authorityResource.getElementsByTagName("Identity").item(0);
      if (authorityResource != null) {
         out.println("Authority ID: "+DomHelper.getValue(identity, "AuthorityID"));
         out.println("Resource Key: "+DomHelper.getValue(identity, "ResourceKey"));
      } //end if authority resource
   }
   
   Element rdbmsResource = VoDescriptionServer.getResource("RdbmsMetadata");
   if (rdbmsResource != null) {
      out.println("<h2>RDBMS Metadata</h2>");

      //<!--- list tables & columns --->
      try {
         NodeList tables = rdbmsResource.getElementsByTagName("Table");
         for (int table=0;table<tables.getLength();table++) {
            Element tableElement = (Element) tables.item(table);
            String tableName = tableElement.getAttribute("name");
%>
            <h2>Table '<%=tableName %>' - <%= DomHelper.getValue(tableElement,"Title") %> </h2>
            <p><%= DomHelper.getValue(tableElement,"Description") %></p>
            <p>
            <table border=1 summary='Column details for table <%=tableName %>' cellpadding='5%'>
            <tr>
            <th>Column</th>
            <th>Type</th>
            <th><a href='<%= UnitDictionary.UNIT_REF %>'>Units</a></th>
            <th><a href='<%= UcdDictionary.UCD1REF %>'>UCD1</a></th>
            <th><a href='<%= UcdDictionary.UCD1PREF %>'>UCD1+</a></th>
            <th>Error</th>
            <th>Description</th>
            </tr>
<%
            NodeList columns = tableElement.getElementsByTagName("Column");
            for (int col=0;col<columns.getLength();col++) {
               Element colElement = (Element) columns.item(col);
%>
               <tr>
               <th><%=colElement.getAttribute("name") %></th>
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
      NodeList funcList = VoDescriptionServer.getVoDescription().getElementsByTagName("Function");
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
   } //end catch
} // end if rdbmsMetadata exists
%>

<h1>Raw Metadata</h1>

<p>Download the metadata document <a href='GetMetadata'>here</a></p>
<p>Download the raw metadata document <a href='getRawMetadata.jsp'>here</a></p>
<%-- <p>If you have access to administrator functions, you can download regenerated metadata <a href='admin/generateMetadata.jsp'>here</a></p> --%>

</div>

   <%@ include file="footer.xml" %>
</body>
</html>


