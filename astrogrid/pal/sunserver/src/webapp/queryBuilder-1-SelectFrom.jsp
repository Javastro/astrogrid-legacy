<%@ page language="java"
    import="java.util.*, java.io.*,
    org.astrogrid.dataservice.metadata.*,
    org.astrogrid.dataservice.service.DataServer,
    org.astrogrid.ucd.*,
    org.w3c.dom.*, org.astrogrid.xml.* " %>

<head>
<title>Query Builder (p1) for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>
<h1>Query Builder (1 - Select/From)</h1>

This form helps you construct a query without messing about with SQL or ADQL.
Just select below what you want to know; if you leave all of the 'Return' checkboxes
empty you will receive all the columns in your results.
<%
   Document metadata = VoDescriptionServer.getVoDescription();
%>
<p>
 <form method="get" action='queryBuilder-2-Constraints.jsp'>

<%
   NodeList tables = metadata.getElementsByTagName("Table");
   if (tables.getLength() == 0) {
      //would like to use assert but some Tomcats can't compile it properly
      throw new RuntimeException("No &lt;Table&gt; element in metadata");
   }
   for (int t=0;t<tables.getLength();t++) {
      Element tableElement = (Element) tables.item(t);
      String tableName = DomHelper.getValueOf(tableElement, "Name");
      String link = DomHelper.getValueOf(tableElement, "Link");
%>
      <h3><%=tableName %></h3>
      <p><%= DomHelper.getValueOf(tableElement, "Description") %></p>
      <% if (link.length()>0) { %> <p><a href='<%=link %>'>Link</a></p> <% } %>
      
      <table border="1" cellspacing="3" cellpadding="3" >
         <tr>
            <td><b>Constrain?</b></td>
            <td><b>Return?</b></td>
            <td><b>Column</b></td>
            <td><b>Units</b></td>
            <td><b><a href='<%= Ucd1Dictionary.REF %>'>UCD1</a></b></td>
            <td><b><a href='<%= Ucd1PlusDictionary.REF %>'>UCD1+</a></b></td>
            <td><b>Description</b></td>
         </tr>

<%
      NodeList columns = tableElement.getElementsByTagName("Column");
      for (int c=0;c<columns.getLength();c++) {
         Element colElement = (Element) columns.item(c);
         String colName = DomHelper.getValueOf(colElement, "Name");
         String colId = tableName+"."+colName;
%>
         <tr>
         <td><input type='checkbox' name='searchColumn' value='<%= colId %>' ></td>
         <td><input type='checkbox' name='resultColumn' value='<%= colId %>' ></td>
         <td><%= colName %></td>
         <td><%= DomHelper.getValueOf(colElement, "Units") %></td>
         <td><%= DomHelper.getValueOf(colElement, "UCD") %></td>
         <td><%= DomHelper.getValueOf(colElement, "UcdPlus") %></td>
         <td><%= DomHelper.getValueOf(colElement, "Description") %></td>

<%
      } //end for cols
%></table><%
   } //end for tables
%>
</p>
<input type='submit' value='Next'>

</form>


<hr>
<h2>Debug</h2>
Parameter names in this request:
<p>
<pre>
<%
   Enumeration e = request.getParameterNames();
   while (e.hasMoreElements()) {
      String key = (String)e.nextElement();
      String[] values = request.getParameterValues(key);
      out.print(" " + key + " = ");
      for(int i = 0; i < values.length; i++) {
         out.print(values[i] + " ");
      }
      out.println();
   }
%>
</pre>
</div>
<%@ include file="footer.xml" %>

</body>
</html>



