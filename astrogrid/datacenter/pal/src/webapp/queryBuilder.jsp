<%@ page language="java"
    import="java.util.*, java.io.*, org.astrogrid.datacenter.sqlparser.*, org.astrogrid.datacenter.query.results.*, org.astrogrid.datacenter.query.criteria.*,
    org.astrogrid.datacenter.metadata.*, org.astrogrid.datacenter.service.HtmlDataServer, org.w3c.dom.*, org.astrogrid.util.* " %>

<!-- returns 'checked' if the given value is in the list of values -->
<%!
   public String getChecked(String values[], String value) {

      if (values != null) {
         for (int i=0;i<values.length;i++) {
            if (values[i].equals(value)) {
               return "checked";
            }
         }
      }
      return "";
   }
%>

<!-- returns 'selected' if the first value is in the second array at the index point.
   returns empty otherwise (or if the index point is out of range) e -->
<%!
   public String getSelected(String value1, String[] values, int index) {

      if ((value1 != null) && (values != null) && (index<values.length) && (value1.equals(values[index]))) {
            return "selected";
      }
      return "";
   }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN"
            "http://www.w3.org/TR/REC-html40/loose.dtd">

<%=HtmlDataServer.getHeadElement("Query Builder") %>

<%
   String[] searchCols = request.getParameterValues("searchColumn");
   if (searchCols == null) { searchCols = new String[0]; }  //don't have to check for nulls
   String[] resultCols = request.getParameterValues("resultColumn");
   if (resultCols == null) { resultCols = new String[0]; }
   String[] conditionColumns = request.getParameterValues("conditionColumn");
   if (conditionColumns == null) { conditionColumns = new String[0]; }
   String[] conditionOperands = request.getParameterValues("conditionOperand");
   if (conditionOperands == null) { conditionOperands = new String[0]; }
   String[] conditionValues = request.getParameterValues("conditionValue");
   if (conditionValues == null) { conditionValues = new String[0]; }
   
   String formAction = "queryBuilder.jsp";
%>

   
<body>
<%=HtmlDataServer.getPageHeader() %>
<h1>Query Builder</h1>

This form helps you construct a query without messing about with SQL or ADQL.
Just select below what you want to know; if you leave all of the 'Return' checkboxes
empty you will receive all the columns in your results.
<%
   Document metadata = null;
   try {
      metadata = MetadataServer.getMetadata();
   }
   catch (FileNotFoundException fnfe) {
      metadata =MetadataGenerator.generateMetadata();
      %>
      <p>Note that a configured metadata file could not be found (<%= fnfe %>)
      so this is based on the plugin's automatically generated metadata.
      <%
   }
%>
<h2>From</h2>

Select which columns you will be using to search:
<p>
 <form method="get" onSubmit="
   this.action=formAction;
   return true;
   ">

<%
   NodeList tables = metadata.getElementsByTagName("Table");
   if (tables.getLength() == 0) {
      //would like to use assert but some Tomcats can't compile it properly
      throw new RuntimeException("No <Table> element in metadata");
   }
   for (int t=0;t<tables.getLength();t++) {
      Element tableElement = (Element) tables.item(t);
      String tableName = tableElement.getAttribute("name");
      String link = DomHelper.getValue(tableElement, "Link");
%>
      <h3><%=tableName %></h3>
      <p><%= DomHelper.getValue(tableElement, "Description") %></p>
      <% if (link.length()>0) { %> <p><a href='<%=link %>'>Link</a></p> <% } %>
      
      <table border="1" cellspacing="3" cellpadding="3" >
         <tr>
            <td><b>Constrain?</b></td>
            <td><b>Return?</b></td>
            <td><b>Table</b></td>
            <td><b>Column</b></td>
            <td><b>Units</b></td>
            <td><b>UCD1</b></td>
            <td><b>UCD1+</b></td>
            <td><b>Description</b></td>
         </tr>

<%
      NodeList columns = tableElement.getElementsByTagName("Column");
      for (int c=0;c<columns.getLength();c++) {
         Element colElement = (Element) columns.item(c);
         String colName = colElement.getAttribute("name");
         String colId = tableName+"."+colName;
         %>
         <tr>
         <td><input type='checkbox' name='searchColumn' value='<%= colId %>' <%= getChecked(searchCols,colId) %> ></td>
         <td><input type='checkbox' name='resultColumn' value='<%= colId %>' <%= getChecked(resultCols,colId) %> ></td>
         <td><%= tableName %></td>
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
<input type='submit' value='Update'>

<h2>Constraints</h2>
Enter below the constraints you want to put on each of the values.

<p>These will be combined in an
<input type='radio' name='combine' value='Intersection' <%=getChecked(request.getParameterValues("combine"), "Intersection") %> /> intersection (AND),
or a <input type='radio' name='combine' value='Union' <%=getChecked(request.getParameterValues("combine"), "Union") %> /> union (OR).

<p>

<table border="1" cellspacing="3" cellpadding="3" >
<%
   //show all conditions, plus one row for adding a new condition
   for (int con=0;con<conditionColumns.length;con++) {
%>
<tr>
<!-- Drop down list to select table/column -->
<td>
  <select name="conditionColumn">
   <option value=""> <!-- won't test this condition if this one selected -->
   <% for (int col=0;col<searchCols.length;col++) { %>
      <option value=<%=searchCols[col] %> <%=getSelected(searchCols[col], conditionColumns, con) %> ><%=searchCols[col] %>
   <% } %>
  </select>
</td>
<!-- Operand -->
<td>
  <select name="conditionOperand">
   <option <%=getSelected("", conditionOperands, con) %> value=""> <!-- won't test this condition if this one selected -->
   <option <%=getSelected("GT", conditionOperands, con) %> value="GT">&gt;
   <option <%=getSelected("GTE", conditionOperands, con) %> value="GTE">&gt;=
   <option <%=getSelected("EQ", conditionOperands, con) %> value="EQ">=
   <option <%=getSelected("LT", conditionOperands, con) %> value="LT">&lt;
   <option <%=getSelected("LTE", conditionOperands, con) %> value="LTE">&lt;=
  </select>
</td>
<td>
    <input type='text' name='conditionValue' value='<%=conditionValues[con] %>' >
</td>
</tr>
<%
   } //end con for loop
   
   //add another row if there aren't any or if the last one is not empty already
   if ((conditionColumns.length==0) || (conditionColumns[conditionColumns.length-1].length()!=0)) {
%>
   <tr>
   <!-- Drop down list to select table/column -->
   <td>
     <select name="conditionColumn">
      <option value=""> <!-- won't test this condition if this one selected -->
      <% for (int col=0;col<searchCols.length;col++) { %>
         <option value=<%=searchCols[col] %>  ><%=searchCols[col] %>
      <% } %>
     </select>
   </td>
   <!-- Operand -->
   <td>
     <select name="conditionOperand">
      <option value=""> <!-- won't test this condition if this one selected -->
      <option value="GT">&gt;
      <option value="GTE">&gt;=
      <option value="EQ">=
      <option value="LT">&lt;
      <option value="LTE">&lt;=
     </select>
   </td>
   <td>
       <input type='text' name='conditionValue' >
   </td>
   </tr>
<%
   } //end if
%>
</table>

</p>

<h2>Submit Query</h2>

(Not yet implemented; create ADQL below then copy and paste to <a href="adqlx-form.html">this page</a>)
<!-- <input type='submit' name='MakeAdql05' value='Make ADQL v0.5'> <!-- onclick='formAction="makeAdql05.jsp";'> -->


<h2>Make ADQL</h2>
These buttons generate ADQL from the above query and display it in the
text area below. This is useful if
you are building a query to be inserted into a workflow for example
<p>

<input type='submit' name='MakeAdql05' value='Make ADQL v0.5'> <!-- onclick='formAction="makeAdql05.jsp";'> -->
<input type='submit' name='MakeAdql074' value='Make ADQL v0.7.4'> <!-- onclick='formAction="makeAdql074.jsp";'> -->
</p>
<!-- Create ADQL from above -->
<%
   Condition criteria = null;
   //create modelled query
   for (int con=0;con<conditionColumns.length;con++) {
    
      if ((conditionColumns[con] != null) && (conditionColumns[con].length() >0)) {
         String colRef = conditionColumns[con];
         String tableName = colRef.substring(0, colRef.indexOf("."));
         String colName = colRef.substring(colRef.indexOf(".")+1);
         
         String operand = "";
         if (conditionOperands[con].equals("GT")) operand = ">";
         else if (conditionOperands[con].equals("GTE")) operand = ">=";
         else if (conditionOperands[con].equals("LT"))  operand = "<";
         else if (conditionOperands[con].equals("LTE")) operand = "<=";
         else if (conditionOperands[con].equals("EQ")) operand = "=";
         else if (conditionOperands[con].equals("LIKE")) operand = "LIKE";
         
         Condition newCriteria = new NumericComparison(
                        new ColumnReference(tableName,colName),
                        operand,
                        new LiteralNumber(conditionValues[con])
                     );
         
         if (criteria == null) {
            criteria = newCriteria;
         }
         else {
            if (request.getParameterValues("combine").equals("Intersection")) {
               //add as AND to previous
               criteria = new LogicalExpression(criteria, "AND", newCriteria);
            }
            else {
               //add as OR to previous
               criteria = new LogicalExpression(criteria, "OR", newCriteria);
            }
         }
      }
      
   }

   //build up list of output columns
   Vector outColRefs = new Vector();
   for (int i = 0; i < resultCols.length; i++) {
      if ((conditionColumns[i] != null) && (conditionColumns[i].length() >0)) {
         String colRef = conditionColumns[i];
         String tableName = colRef.substring(0, colRef.indexOf("."));
         String colName = colRef.substring(colRef.indexOf(".")+1);
         outColRefs.add(new ColumnReference(tableName, colName));
      }
   }
   
   
   Query query = new Query(searchCols,
                           criteria,
                           new TableResultsDefinition(null, (NumericExpression[]) outColRefs.toArray(new NumericExpression[] {})));
         
   String adqlXml = "";
   if (request.getParameter("MakeAdql074") != null) {
      adqlXml = Query2Adql074.makeAdql(query, "Generated from queryBuilder");
   }
   else if (request.getParameter("MakeAdql05") != null) {
      adqlXml = Query2Adql05.makeAdql(query, "Generated from queryBuilder");
   }

%>

<textarea name="AdqlXml" rows='50' cols='100'><%=adqlXml %></textarea></td>
<!--
<h2>Submit Query</h2>
At the moment, this asks an asynchronous query (ie, your browser will wait until
the query is complete), and the results are returned to your browser.
<p>

<input type='submit' value='Ask Query' onClick="window.nextPage='askQuery.jsp'">
-->
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

<%=HtmlDataServer.getPageFooter() %>

</body>
</html>




