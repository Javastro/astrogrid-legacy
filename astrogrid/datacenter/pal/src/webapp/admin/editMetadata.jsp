<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.util.DomHelper,
       org.astrogrid.datacenter.metadata.*,
       org.astrogrid.datacenter.service.*"
   isThreadSafe="false"
   session="false"
%>
<html>
<head>
<title>Metadata Editor for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("../style/maven-base.css");
          @import url("../style/maven-theme.css");
</style>
</title>
</head>

<body>
<%@ include file='../header.xml' %>
<%--don't include this as the links aren't right include file='../navigation.xml' --%>

<div id='bodyColumn'>
<h1>Metadata Editor for <%=DataServer.getDatacenterName() %></h1>

<form action='mergeEditMeta.jsp'>
<!--- list tables & columns --->
<%
   Document metadata = VoDescriptionServer.getVoDescription();

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
</div>

<%@ include file='../footer.xml' %>
</body>
</html>


