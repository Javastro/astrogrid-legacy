<%@ page language="java"
    import="java.util.*, org.astrogrid.config.SimpleConfig, java.io.*, org.astrogrid.datacenter.sqlparser.*,
    org.astrogrid.datacenter.returns.*, org.astrogrid.datacenter.query.condition.*,
    org.astrogrid.datacenter.metadata.*, org.astrogrid.datacenter.service.ServletHelper,
    org.astrogrid.datacenter.service.DataServer,
    org.astrogrid.datacenter.ucd.UcdDictionary,
    org.w3c.dom.*, org.astrogrid.util.* " %>

<head>
<title>Query Builder (p1) for <%=DataServer.getDatacenterName() %> </title>
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
      throw new RuntimeException("No <Table> element in metadata");
   }
   for (int t=0;t<tables.getLength();t++) {
      Element tableElement = (Element) tables.item(t);
      String tableName = DomHelper.getValue(tableElement, "Name");
      String link = DomHelper.getValue(tableElement, "Link");
%>
      <h3><%=tableName %></h3>
      <p><%= DomHelper.getValue(tableElement, "Description") %></p>
      <% if (link.length()>0) { %> <p><a href='<%=link %>'>Link</a></p> <% } %>
      
      <table border="1" cellspacing="3" cellpadding="3" >
         <tr>
            <td><b>Constrain?</b></td>
            <td><b>Return?</b></td>
            <td><b>Column</b></td>
            <td><b>Units</b></td>
            <td><b><a href='<%= UcdDictionary.UCD1REF %>'>UCD1</a></b></td>
            <td><b><a href='<%= UcdDictionary.UCD1PREF %>'>UCD1+</a></b></td>
            <td><b>Description</b></td>
         </tr>

<%
      NodeList columns = tableElement.getElementsByTagName("Column");
      for (int c=0;c<columns.getLength();c++) {
         Element colElement = (Element) columns.item(c);
         String colName = DomHelper.getValue(colElement, "Name");
         String colId = tableName+"."+colName;
%>
         <tr>
         <td><input type='checkbox' name='searchColumn' value='<%= colId %>' ></td>
         <td><input type='checkbox' name='resultColumn' value='<%= colId %>' ></td>
         <td><%= colName %></td>
         <td><%= DomHelper.getValue(colElement, "Units") %></td>
         <td><%= DomHelper.getValue(colElement, "UCD") %></td>
         <td><%= DomHelper.getValue(colElement, "UcdPlus") %></td>
         <td><%= DomHelper.getValue(colElement, "Description") %></td>

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



